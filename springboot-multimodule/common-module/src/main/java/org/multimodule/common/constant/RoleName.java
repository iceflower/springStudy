package org.multimodule.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author 김영근
 *
 */
public enum RoleName {

    ROLE_ADMIN("ROLE_ADMIN", "관리자 회원"),

    ROLE_USER("ROLE_USER", "일반 회원");
	
	@JsonValue
	private String code;
	private String textString;
	private RoleName(String code, String textString) {
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
