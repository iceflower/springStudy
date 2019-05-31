package org.multimodule.common.entity.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "비밀번호 초기화 링크 요청", description = "비밀번호 초기화 링크 요청 payload")
public class PasswordResetLinkRequest {

    @NotBlank(message = "이메일 정보를 찾을 수 없습니다.")
    @ApiModelProperty(value = "유저가 등록한 이메일", required = true, allowableValues = "문자열(NonEmpty)")
    private String email;

    public PasswordResetLinkRequest(String email) {
        this.email = email;
    }

    public PasswordResetLinkRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
