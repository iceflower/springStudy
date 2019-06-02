package org.multimodule.common.entity.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.multimodule.common.constant.DeviceType;
import org.multimodule.common.validation.annotation.NullOrNotBlank;

import io.swagger.annotations.ApiModelProperty;

public class DeviceInfo {

    @NotBlank(message = "디바이스ID가 존재하지 않습니다. (NotBlank)")
    @ApiModelProperty(value = "디바이스ID(uuid)", required = true, dataType = "문자열", allowableValues = "문자열(Not Empty)")
    private String deviceId;

    @NotNull(message = "deviceType이 존재하지 않습니다. (NotNull)")
    @ApiModelProperty(value = "디바이스 타입 (Android/iOS)", required = true, dataType = "문자열", allowableValues = "DEVICE_TYPE_ANDROID, DEVICE_TYPE_IOS, DEVICE_TYPE_WEB")
    private DeviceType deviceType;

    @NullOrNotBlank(message = "디바이스 notification 는 null은 허용되지만 빈칸(empty space)은 허용되지 않습니다.")
    @ApiModelProperty(value = "Device notification id", dataType = "string", allowableValues = "문자열(Non empty)")
    private String notificationToken;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, DeviceType deviceType, String notificationToken) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.notificationToken = notificationToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

	@Override
	public String toString() {
		return "DeviceInfo [deviceId=" + deviceId + ", deviceType=" + deviceType + ", notificationToken="
				+ notificationToken + "]";
	}
    
}
