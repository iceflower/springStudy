package org.multimodule.common.entity.payload;
import javax.validation.constraints.NotBlank;

import org.multimodule.common.validation.annotation.MatchPassword;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@MatchPassword
@ApiModel(value = "비밀번호 초기화 요청", description = "비밀번호 초기화 요청 payload")
public class PasswordResetRequest {

    @NotBlank(message = "신규입력 비밀번호 정보를 찾을 수 없습니다.")
    @ApiModelProperty(value = "사용자가 새롭게 입력한 비밀번호", required = true, allowableValues = "문자열(NonEmpty)")
    private String password;

    @NotBlank(message = "재입력 비밀번호 정보를 찾을 수 없습니다.")
    @ApiModelProperty(value = "신규입력 비밀번호 정보와 같아야 한다. 그렇지 않으면 예외가 발생한다.", required = true, allowableValues = "문자열(NonEmpty, 신규입력 비밀번호와 일치)")
    private String confirmPassword;

    @NotBlank(message = "비밀번호 초기화 링크 요청시 제공한 토큰")
    @ApiModelProperty(value = "이메일 전송시 받은 초기화 토큰", required = true, allowableValues = "문자열(NonEmpty)")
    private String token;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String password, String confirmPassword, String token) {
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.token = token;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
