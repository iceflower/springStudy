package org.multimodule.common.entity.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.multimodule.common.constant.DeviceType;
import org.multimodule.common.validation.annotation.NullOrNotBlank;


// import com.accolite.pru.health.AuthApp.model.DeviceType;

// import io.swagger.annotations.ApiModelProperty;

public class DeviceInfo {

    @NotBlank(message = "Device id cannot be blank")
   //  @ApiModelProperty(value = "Device Id", required = true, dataType = "string", allowableValues = "Non empty string")
    private String deviceId;

    @NotNull(message = "Device type cannot be null")
    // @ApiModelProperty(value = "Device type Android/iOS", required = true, dataType = "string", allowableValues = "DEVICE_TYPE_ANDROID, DEVICE_TYPE_IOS")
    private DeviceType deviceType;

    @NullOrNotBlank(message = "Device notification token can be null but not blank")
    // @ApiModelProperty(value = "Device notification id", dataType = "string", allowableValues = "Non empty string")
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