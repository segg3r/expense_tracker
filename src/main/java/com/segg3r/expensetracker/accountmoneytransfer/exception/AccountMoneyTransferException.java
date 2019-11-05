package com.segg3r.expensetracker.accountmoneytransfer.exception;

public class AccountMoneyTransferException extends Exception {
	public AccountMoneyTransferException() {
	}

	public AccountMoneyTransferException(String message) {
		super(message);
	}

	public AccountMoneyTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountMoneyTransferException(Throwable cause) {
		super(cause);
	}

	public AccountMoneyTransferException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
