package org.multimodule.api.auth.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.multimodule.api.auth.event.OnGenerateResetLinkEvent;
import org.multimodule.api.auth.event.OnRegenerateEmailVerificationEvent;
import org.multimodule.api.auth.event.OnUserAccountChangeEvent;
import org.multimodule.api.auth.event.OnUserRegistrationCompleteEvent;
import org.multimodule.api.auth.service.AuthService;
import org.multimodule.api.auth.service.UserService;
import org.multimodule.common.entity.CustomUserDetails;
import org.multimodule.common.entity.PasswordResetToken;
import org.multimodule.common.entity.User;
import org.multimodule.common.entity.payload.ApiResponse;
import org.multimodule.common.entity.payload.JwtAuthenticationResponse;
import org.multimodule.common.entity.payload.LoginRequest;
import org.multimodule.common.entity.payload.PasswordResetLinkRequest;
import org.multimodule.common.entity.payload.PasswordResetRequest;
import org.multimodule.common.entity.payload.RegistrationRequest;
import org.multimodule.common.entity.payload.TokenRefreshRequest;
import org.multimodule.common.entity.token.EmailVerificationToken;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import reactor.core.publisher.Mono;


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
    private final UserService userService;
    
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthController(AuthService authService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 이메일 중복체크 여부 확인
     */
    @ApiOperation(value = "이메일 중복체크용 api")
    @GetMapping("/checkEmailInUse")
    //아래 어노테이션은 API 앱을 카나리 배포할 떄 응용하고자 작성해본 것. v1과 v2를 동시에 유지하는 것이 가능한지 연구해볼 것
    //@RequestMapping(value="/checkEmailInUse", method = RequestMethod.GET, produces = "application/vnd.auth.api.v1+json")
    public Mono<ResponseEntity<?>> checkEmailInUse(@ApiParam(value = "Email id to check against") @RequestParam("email") String email) {
        Mono<Boolean> emailExists = authService.emailAlreadyExists(email);
        return Mono.just(ResponseEntity.ok(new ApiResponse(emailExists, true)));
    }

    /**
     * 유저ID 중복여부 확인
     */
    @ApiOperation(value = "유저ID 중복체크용 api")
    @GetMapping("/checkUsernameInUse")
    public Mono<ResponseEntity<?>> checkUsernameInUse(@ApiParam(value = "중복체크 하기 위한 변수") @RequestParam(
            "username") String username) {
        Mono<Boolean> usernameExists = authService.usernameAlreadyExists(username);
        return Mono.just(ResponseEntity.ok(new ApiResponse(usernameExists.block(), true)));
    }


	/*
	 @PostMapping("/login")
	 * 
	 * @ApiOperation(value =
	 * "로그인 인증 처리 (시스템 내에 JWT 기반 세션 생성 및 생성 완료된 토큰값 클라이언트에 리턴)") public
	 * Mono<ResponseEntity<?>> authenticateUser(@ApiParam(value =
	 * "로그인 요청용 payload") @Valid @RequestBody LoginRequest loginRequest) {
	 * Authentication authentication = authService.authenticateUser(loginRequest)
	 * .orElseThrow(() -> new UserLoginException("로그인 요청을 처리할 수 없습니다. [" +
	 * loginRequest + "]"));
	 * 
	 * CustomUserDetails customUserDetails = (CustomUserDetails)
	 * authentication.getPrincipal(); logger.info("로그인 성공 : " +
	 * customUserDetails.getUsername());
	 * SecurityContextHolder.getContext().setAuthentication(authentication);
	 * RefreshToken refreshToken =
	 * authService.createAndPersistRefreshTokenForDevice(authentication,
	 * loginRequest).orElseThrow(() -> new UserLoginException("토큰을 최신화할 수 없습니다. : ["
	 * + loginRequest + "]"));
	 * 
	 * String jwtToken = authService.generateToken(customUserDetails);
	 * 
	 * return Mono.just(ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken,
	 * refreshToken.getToken(), tokenProvider.getExpiryDuration())));
	 * 
	 * }
	 */

    @PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@ApiParam(value ="로그인 요청용 payload") @Valid @RequestBody LoginRequest loginRequest) {
	
		 Authentication authentication = authService.authenticateUser(loginRequest)
				 .orElseThrow(() -> new UserLoginException("로그인 요청을 처리할 수 없습니다. [" +loginRequest + "]"));
		 
		 
		 CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		 logger.info("로그인 성공 : " + customUserDetails.getUsername());
		 
		 
		 String jwtToken = authService.generateToken(customUserDetails);
		 
		 
		 return Mono.just(ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken)));
		
	}
    
    
    
    /**
     * 유저 신규등록 API. 
     * 신규등록 성공 시 인증메일 토큰 이벤트를 발생시킴.
     */
    @PostMapping("/register")
    @ApiOperation(value = "유저 신규등록용 API")
    public Mono<ResponseEntity<?>> registerUser(@ApiParam(value = "사용자 신규등록 요청용 payload") @Valid @RequestBody RegistrationRequest registrationRequest) {

    	
    	User user = authService.registerUser(registrationRequest)
    			.orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(), "DB에서 유저 정보 객체를 찾을 수 없습니다."));

    	
    	
//    	UriComponentsBuilder base = ServletUriComponentsBuilder.fromCurrentContextPath().path("/en");
//    	MvcUriComponentsBuilder builder = MvcUriComponentsBuilder.relativeTo(base);
//    	builder.withMethodCall(on(BookingController.class).getBooking(21)).buildAndExpand(42);

    	//URI uri = uriComponents.encode().toUri();
    	
    	String fullContextPath = System.getProperty("server.servlet.context-path") + "/api/auth/registrationConfirmation";
    	//UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
    	UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/api/auth/registrationConfirmation");  
        applicationEventPublisher.publishEvent(new OnUserRegistrationCompleteEvent(user, urlBuilder));
        logger.info("[API] 사용자 신규등록 완료 : " + user);
        return Mono.just(ResponseEntity.ok(new ApiResponse("사용자 신규등록이 성공하였습니다. 이메일로 전송된 인증메일을 확인하세요.", true)));
    	
    }

    /**
     * Receives the reset link request and publishes an event to send email id containing
     * the reset link if the request is valid. In future the deeplink should open within
     * the app itself.
     */
    @PostMapping("/password/resetlink")
    @ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
            "reset link")
    public Mono<ResponseEntity<?>> resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {

    	PasswordResetToken passwordResetToken = authService.generatePasswordResetToken(passwordResetLinkRequest)
    			.orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(), "유효한 토큰 생성에 실패하였습니다."));
    	
    	
    	UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/password/reset");  

        applicationEventPublisher.publishEvent(new OnGenerateResetLinkEvent(passwordResetToken, urlBuilder));
        
        return Mono.just(ResponseEntity.ok(new ApiResponse("비밀번호 재설정 이메일을 성공적으로 전송하였습니다.", true)));
    	
    }

    /**
     * 비밀번호 초기화 요청 접수용 API
     */

    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement email")
    public Mono<ResponseEntity<?>> resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
    	
    	
    	User changedUser = authService.resetPassword(passwordResetRequest).orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    	applicationEventPublisher.publishEvent(new OnUserAccountChangeEvent(changedUser, "비밀번호 재설정","재설정 성공"));
    	return Mono.just(ResponseEntity.ok(new ApiResponse("Password changed successfully", true)));
    }

    /**
     * 인증메일을 통한 회원가입 인증 완료 처리용 API
     */
    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "유저 등록시 기입된 이메일로 전송된 인증이메일을 확정하는 API")
    public Mono<ResponseEntity<?>> confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {

    	authService.confirmEmailRegistration(token).orElseThrow(() -> new InvalidTokenRequestException("이메일 인증 토큰", token, "승인 실패! 인증 메일을 다시 요청하세요."));
    	return Mono.just(ResponseEntity.ok(new ApiResponse("유저 인증 성공", true)));
    }

    /**
     * 인증 이메일 재전송용 API
     */
    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "토큰을 새롭게 다시 만들어 인증메일을 재전송함. 발송한 지 오래된 과거 인증메일의 토큰을 통해 새롭게 키를 받아 인증하려 한다는 가정을 해도 무방함 (그 키는 아직 저장되어있을 수도 있지만, 이미 삭제되어 있을 수도 있음)")
    public Mono<ResponseEntity<?>> resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user email after registration") @RequestParam("token") String existingToken) {

        EmailVerificationToken newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "User is already registered. No need to re-generate token"));

        User registeredUser = Optional.ofNullable(newEmailToken.getUser())
        		.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No user associated with this request. Re-verification denied"));

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/api/auth/registrationConfirmation");
        applicationEventPublisher.publishEvent(new OnRegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken));
        return Mono.just(ResponseEntity.ok(new ApiResponse("Email verification resent successfully", true)));
        
    }

}
