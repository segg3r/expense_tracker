package com.segg3r.expensetracker.account.exception;

public class AccountDeletionException extends Exception {
	public AccountDeletionException() {
	}

	public AccountDeletionException(String message) {
		super(message);
	}

	public AccountDeletionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountDeletionException(Throwable cause) {
		super(cause);
	}

	public AccountDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
