package jdh.example.chat.model.api;

public enum ApiResponseCode {
	OK("요청 성공"),
	BAD_PARAMETER("요청 파라미터가 잘못되었습니다."),
	NOT_FOUND("리소스를 찾지 못했습니다."),
	UNAUTHORIZED("인증에 실패하였습니다."),
	SERVER_ERROR("서버 에러입니다."),
	DUPLICATE_MEMBER("중복되는 사용자입니다.");
	
	public final String message;
	
	ApiResponseCode(String message) {
		this.message = message;
	}
	
	public String getId() {
		return this.name();
	}
	
	public String getText() {
		return this.message;
	}
}
