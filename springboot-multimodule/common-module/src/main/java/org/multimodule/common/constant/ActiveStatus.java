package org.multimodule.common.constant;

@SuppressWarnings("unused")
public enum ActiveStatus {
	
	DELETED(0,"삭제됨"),
	JUST_REGISTERED(10, "방금 가입함"),
	WAIT_EMAIL_CONFORMATION(11, "이메일 인증 대기중"),
	FAILED_EMAIL_CONFORMATION(12, "이메일 인증 실패"),
	SUCCEED_EMAIL_CONFORMATION(13, "이메일 인증 성공"),
	NORMAL(100,"정상");
	
	private int code;
	private String txt;
	
	ActiveStatus(int code, String txt) {
		this.code = code;
		this.txt = txt;
	}

	

}
