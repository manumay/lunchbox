package org.brainteam.lunchbox.json;

public class JsonException {

	private Long errorCode;
	private String error;
	private String message;
	
	public Long getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(Long errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
