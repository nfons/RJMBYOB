package com.rjmetrics.api.utils;
/**
 * This class is used by the database API to send messages to the API user.
 * other wise, we would get "malformed json" errors. by using this class, we get a clean way to add JSON error messages to inform
 * the api user of any issues
 * @author nate
 *
 */
public class Message {
	
	private int code;
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
