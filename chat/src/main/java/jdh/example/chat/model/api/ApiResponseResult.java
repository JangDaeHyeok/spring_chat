package jdh.example.chat.model.api;

public enum ApiResponseResult {
	SUCEESS("success"),
	FAIL("fail");
	
	public final String message;
	
	ApiResponseResult(String message) {
		this.message = message;
	}
	
	public String getId() {
		return this.name();
	}
	
	public String getText() {
		return this.message;
	}
}
