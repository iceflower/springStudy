package org.multimodule.api.auth.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.multimodule.api.auth.event.OnGenerateResetLinkEvent;
import org.multimodule.api.auth.event.OnRegenerateEmailVerificationEvent;
import org.multimodule.api.auth.event.OnUserAccountChangeEvent;
import org.multimodule.api.auth.event.OnUserRegistrationCompleteEvent;
import org.multimodule.api.auth.service.AuthService;
import org.multimodule.api.security.JwtTokenProvider;
import org.multimodule.common.entity.CustomUserDetails;
import org.multimodule.common.entity.payload.ApiResponse;
import org.multimodule.common.entity.payload.JwtAuthenticationResponse;
import org.multimodule.common.entity.payload.LoginRequest;
import org.multimodule.common.entity.payload.PasswordResetLinkRequest;
import org.multimodule.common.entity.payload.PasswordResetRequest;
import org.multimodule.common.entity.payload.RegistrationRequest;
import org.multimodule.common.entity.payload.TokenRefreshRequest;
import org.multimodule.common.entity.token.EmailVerificationToken;
import org.multimodule.common.entity.token.RefreshToken;
import org.multimodule.common.exception.InvalidTokenRequestException;
import org.multimodule.common.exception.PasswordResetException;
import org.multimodule.common.exception.PasswordResetLinkException;
import org.multimodule.common.exception.TokenRefreshException;
import org.multimodule.common.exception.UserLoginException;
import org.multimodule.common.exception.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 
 * @author 김영근
 * @description 로그인을 ㅇ하지 않은 상태에서만 작동하는 API임
 */

@RestController
@RequestMapping("/api/auth")
//@Api(value = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not logged in. It's not secured by default.")

@Api(value="인증(Authorization) 관련 REST API")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider tokenProvider, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 이메일 중복체크 여부 확인
     */
    @ApiOperation(value = "이메일 중복체크용 api")
    @GetMapping("/checkEmailInUse")
    //아래 어노테이션은 API 앱을 카나리 배포할 떄 응용하고자 작성해본 것. v1과 v2를 동시에 유지하는 것이 가능한지 연구해볼 것
    //@RequestMapping(value="/checkEmailInUse", method = RequestMethod.GET, produces = "application/vnd.auth.api.v1+json")
    public ResponseEntity<?> checkEmailInUse(@ApiParam(value = "Email id to check against") @RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponse(emailExists, true));
    }

    /**
     * 유저ID 중복여부 확인
     */
    @ApiOperation(value = "유저ID 중복체크용 api")
    @GetMapping("/checkUsernameInUse")
    public ResponseEntity<?> checkUsernameInUse(@ApiParam(value = "중복체크 하기 위한 변수") @RequestParam(
            "username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponse(usernameExists, true));
    }


    @PostMapping("/login")
    @ApiOperation(value = "로그인 인증 처리 (시스템 내에 JWT 기반 세션 생성 및 생성 완료된 토큰값 클라이언트에 리턴)")
    public ResponseEntity<?> authenticateUser(@ApiParam(value = "로그인 요청용 payload") @Valid @RequestBody LoginRequest loginRequest) {
    	logger.info("loginRequest : " + loginRequest);
        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("로그인 요청을 처리할 수 없습니다. [" + loginRequest + "]"));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("[API] 로그인 성공 : " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(customUserDetails);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("세션 토큰을 최신화할 수 없습니다. : [" + loginRequest + "]"));
    }

    /**
     * 유저 신규등록 API. 
     * 신규등록 성공 시 인증메일 토큰 이벤트를 발생시킴.
     */
    @PostMapping("/register")
    @ApiOperation(value = "유저 신규등록용 API")
    public ResponseEntity<?> registerUser(@ApiParam(value = "사용자 신규등록 요청용 payload") @Valid @RequestBody RegistrationRequest registrationRequest) {

        return authService.registerUser(registrationRequest)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("[API] 사용자 신규등록 완료 : " + user);
                    return ResponseEntity.ok(new ApiResponse("사용자 신규등록이 성공하였습니다. 이메일로 전송된 인증메일을 확인하세요.", true));
                })
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "DB에서 유저 정보 객체를 찾을 수 없습니다."));
    }

    /**
     * Receives the reset link request and publishes an event to send email id containing
     * the reset link if the request is valid. In future the deeplink should open within
     * the app itself.
     */
    @PostMapping("/password/resetlink")
    @ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
            "reset link")
    public ResponseEntity<?> resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {

        return authService.generatePasswordResetToken(passwordResetLinkRequest)
                .map(passwordResetToken -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
                    OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
                            urlBuilder);
                    applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
                    return ResponseEntity.ok(new ApiResponse("비밀번호 재설정 이메일을 성공적으로 전송하였습니다.", true));
                })
                .orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(), "유효한 토큰 생성에 실패하였습니다."));
    }

    /**
     * 비밀번호 초기화 요청 접수용 API
     */

    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement email")
    public ResponseEntity<?> resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

        return authService.resetPassword(passwordResetRequest)
                .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    }

    /**
     * 인증메일을 통한 회원가입 인증 완료 처리용 API
     */
    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "유저 등록시 기입된 이메일로 전송된 인증이메일을 확정하는 API")
    public ResponseEntity<?> confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {

        return authService.confirmEmailRegistration(token)
                .map(user -> ResponseEntity.ok(new ApiResponse("User verified successfully", true)))
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token, "Failed to confirm. Please generate a new email verification request"));
    }

    /**
     * 인증 이메일 재전송용 API
     */
    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
            "assume that the user would always click on the last re-verification email and " +
            "any attempts at generating new token from past (possibly archived/deleted)" +
            "tokens should fail and report an exception. ")
    public ResponseEntity<?> resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user email after registration") @RequestParam("token") String existingToken) {

        EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token"));

        return Optional.ofNullable(newEmailToken.getUser())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true));
                })
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No user associated with this request. Re-verification denied"));
    }

    /**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     */
    @PostMapping("/refresh")
    @ApiOperation(value = "만료된 인증 토큰을 새롭게 발급해서 리턴해주는 API")
    public ResponseEntity<?> refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updatedToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Unexpected error during token refresh. Please logout and login again."));
    }
}
