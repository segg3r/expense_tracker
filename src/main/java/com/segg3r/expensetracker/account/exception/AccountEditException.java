package com.segg3r.expensetracker.account.exception;

public class AccountEditException extends AccountException {
	public AccountEditException() {
	}

	public AccountEditException(String message) {
		super(message);
	}

	public AccountEditException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountEditException(Throwable cause) {
		super(cause);
	}

	public AccountEditException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
