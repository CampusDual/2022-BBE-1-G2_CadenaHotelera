package com.ontimize.hr.model.core.service.exception;

public class MergeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1745916453946148425L;
	
	public MergeException() {
		super();
	}

	public MergeException(String message) {
		super(message);
	}
	
	public MergeException(Throwable e) {
		super(e);
	}
	
	public MergeException(String message, Throwable e) {
		super(message, e);
	}	
	
}
