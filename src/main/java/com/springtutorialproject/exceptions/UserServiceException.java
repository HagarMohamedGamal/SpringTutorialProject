package com.springtutorialproject.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -814245946395820547L;

	public UserServiceException(String message) {
		super(message);
	}
}
