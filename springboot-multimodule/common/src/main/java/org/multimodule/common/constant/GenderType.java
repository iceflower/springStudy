package org.multimodule.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderType {
	
	MALE("MALE","남자"),
	FEMALE("FEMALE", "여자"),
	UNKNOWN("UNKNOWN", "미입력");
	
	@JsonValue
	private String code;
	private String textString;
	
	GenderType(String code, String textString) {
		this.code = code;
		this.textString = textString;
	}

	public String getTextString() {
		return textString;
	}

	public String getCode() {
		return code;
	}

	
	
	

}
