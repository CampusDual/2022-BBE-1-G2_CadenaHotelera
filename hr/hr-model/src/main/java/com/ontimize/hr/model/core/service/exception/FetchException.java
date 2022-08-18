package com.ontimize.hr.model.core.service.exception;

public class FetchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -243336078911654462L;

	
	public FetchException() {
		super();
	}
	
	public FetchException(String message) {
		super(message);
	}
	
	public FetchException(Throwable e) {
		super(e);
	}
	
	public FetchException(String message, Throwable e) {
		super(message,e);
	}
}
