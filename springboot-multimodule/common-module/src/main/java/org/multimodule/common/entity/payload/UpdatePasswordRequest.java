package org.multimodule.common.entity.payload;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "비밀번호 수정 요청", description = "비밀번호 수정 요청 payload")
public class UpdatePasswordRequest {

	@NotNull(message = "기존 비밀번호 정보를 찾을 수 없습니다.")
    @NotBlank(message = "기존 비밀번호 정보를 찾을 수 없습니다.")
    @ApiModelProperty(value = "유효한 현재 사용자 비밀번호", required = true, allowableValues = "문자열(NonEmpty)")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호를 찾을 수 없습니다.")
    @ApiModelProperty(value = "유효한 새로운 비밀번호", required = true, allowableValues = "문자열(NonEmpty)")
    private String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordRequest() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatePasswordRequest [oldPassword=");
		builder.append(oldPassword);
		builder.append(", newPassword=");
		builder.append(newPassword);
		builder.append("]");
		return builder.toString();
	}
}
