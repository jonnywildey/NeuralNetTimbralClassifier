package com.exceptions;

public class NoNeuronsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoNeuronsException() {
		super();
		System.out.println("Make some neurons!");
	}
}
