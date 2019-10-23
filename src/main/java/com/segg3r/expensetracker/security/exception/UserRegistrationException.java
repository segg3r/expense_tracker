package com.segg3r.expensetracker.security.exception;

public class UserRegistrationException extends Exception {

	public UserRegistrationException() {
	}

	public UserRegistrationException(String message) {
		super(message);
	}

	public UserRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserRegistrationException(Throwable cause) {
		super(cause);
	}

	public UserRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
