package com.ontimize.hr.model.core.service.exception;

public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5227702100176299284L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(Throwable e) {
		super(e);
	}
	
	public ValidationException(String message,Throwable e) {
		super(message,e);
	}
	
}
