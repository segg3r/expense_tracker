package com.segg3r.expensetracker.account.exception;

public class AccountCreationException extends AccountException {

	public AccountCreationException() {
	}

	public AccountCreationException(String message) {
		super(message);
	}

	public AccountCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountCreationException(Throwable cause) {
		super(cause);
	}

	public AccountCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
