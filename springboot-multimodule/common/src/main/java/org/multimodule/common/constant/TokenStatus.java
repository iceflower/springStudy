package org.multimodule.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TokenStatus {
    /**
     * Token is in pending state awaiting user confirmation
     */
    STATUS_PENDING("STATUS_PENDING", "대기중"),

    /**
     * Token has been confirmed successfully by the user
     */
    STATUS_CONFIRMED("STATUS_CONFIRMED", "완료");
	
	@JsonValue
	private String code;
	private String textString;
	private TokenStatus(String code, String textString) {
		this.code = code;
		this.textString = textString;
	}
	public String getCode() {
		return code;
	}
	public String getTextString() {
		return textString;
	}
}
