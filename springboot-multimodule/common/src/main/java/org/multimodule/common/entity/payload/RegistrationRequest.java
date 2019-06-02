package org.multimodule.common.entity.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "회원 등록 요청", description = "회원 등록 요청payload")
public class RegistrationRequest {

	@NotNull(message = "성함은 필수적으로 입력하셔야 합니다.")
	@NotBlank(message = "성함은 필수적으로 입력하셔야 합니다.")
    @ApiModelProperty(value = "유효한 유저명 문자열", allowableValues = "문자열(NonEmpty)")
    private String username;

	@NotNull(message = "이메일은 필수적으로 입력하셔야 합니다.")
	@NotBlank(message = "이메일은 필수적으로 입력하셔야 합니다.")
    @ApiModelProperty(value = "유효한 이메일", required = true, allowableValues = "문자열(NonEmpty)")
    private String email;

    @NotNull(message = "등록자 성함은 필수적으로 입력하셔야 합니다.")
    @ApiModelProperty(value = "유효한 비밀번호 문자열", required = true, allowableValues = "문자열(NonEmpty)")
    private String password;

    @NotNull(message = "관리자 등록 유무 정보가 존재하지 않습니다.")
    @ApiModelProperty(value = "관리자 등록 유무 flag", required = true, dataType = "boolean", allowableValues = "true, false")
    private Boolean registerAsAdmin;

    public RegistrationRequest(String username, String email, String password, Boolean registerAsAdmin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerAsAdmin = registerAsAdmin;
    }

    public RegistrationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRegisterAsAdmin() {
        return registerAsAdmin;
    }

    public void setRegisterAsAdmin(Boolean registerAsAdmin) {
        this.registerAsAdmin = registerAsAdmin;
    }
}
