package org.multimodule.common.entity.payload;

/*import com.accolite.pru.health.AuthApp.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;*/

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.multimodule.common.validation.annotation.NullOrNotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "로그인 요청 모델", description = "로그인 요청 payload")
public class LoginRequest {
    @NullOrNotBlank(message = "로그인 이메일을 입력하세요.")
    @ApiModelProperty(value = "유저가 등록한 이메일", required = true, allowableValues = "비지 않은 문자열")
    private String email;

    @NotNull(message = "로그인 비밀번호를 입력하세요.")
    @ApiModelProperty(value = "유효한 비밀번호", required = true, allowableValues = "NonEmpty String")
    private String password;

    @Valid
    @NotNull(message = "디바이스 정보를 입력하세요.")
    @ApiModelProperty(value = "디바이스 정보", required = true, dataType = "객체", allowableValues = "유효한  deviceInfo 객체")
    private DeviceInfo deviceInfo;

    public LoginRequest(String email, String password, DeviceInfo deviceInfo) {
        this.email = email;
        this.password = password;
        this.deviceInfo = deviceInfo;
    }

    public LoginRequest() {
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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

	@Override
	public String toString() {
		return "LoginRequest [email=" + email + ", password=" + password + ", deviceInfo=" + deviceInfo + "]";
	}
    
}
