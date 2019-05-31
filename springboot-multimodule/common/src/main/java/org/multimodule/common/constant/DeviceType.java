package org.multimodule.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceType {
	DEVICE_TYPE_ANDROID("DEVICE_TYPE_ANDROID"),
	DEVICE_TYPE_IOS("DEVICE_TYPE_IOS");

	@JsonValue
    private final String code;
    
    DeviceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
		return code;
	}
    
}