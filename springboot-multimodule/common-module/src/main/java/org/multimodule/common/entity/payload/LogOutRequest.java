package org.multimodule.common.entity.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "로그아웃 요청", description = "로그아웃 요청 payload")
public class LogOutRequest {

    @Valid
    @NotNull(message = "디바이스 정보가 존재하지 않습니다.")
    @ApiModelProperty(value = "디바이스 정보", required = true, dataType = "객체", allowableValues = "유효한 디바이스 정보 객체")
    private DeviceInfo deviceInfo;

    public LogOutRequest() {
    }

    public LogOutRequest(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
