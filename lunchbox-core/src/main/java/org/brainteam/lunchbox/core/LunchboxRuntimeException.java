package org.brainteam.lunchbox.core;

public class LunchboxRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LunchboxRuntimeException() {
		super();
	}
	
	public LunchboxRuntimeException(String msg) {
		super(msg);
	}
	
	public LunchboxRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
