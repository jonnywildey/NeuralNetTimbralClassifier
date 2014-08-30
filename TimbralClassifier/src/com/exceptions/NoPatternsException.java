package com.exceptions;

public class NoPatternsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoPatternsException() {
		super();
		System.out.println("You haven't loaded any patterns!");
	}
}
