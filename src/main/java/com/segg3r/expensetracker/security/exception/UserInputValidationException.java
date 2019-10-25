package com.segg3r.expensetracker.security.exception;

public class UserInputValidationException extends SecurityException {

	public UserInputValidationException() {
	}

	public UserInputValidationException(String message) {
		super(message);
	}

	public UserInputValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserInputValidationException(Throwable cause) {
		super(cause);
	}

	public UserInputValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
