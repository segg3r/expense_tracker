package com.segg3r.expensetracker.account;

import lombok.Data;

@Data
public class AccountMoneyTransfer {
	private String fromAccountId;
	private String toAccountId;
	/**
	 * Last two decimals are cents to avoid floating point errors.
	 */
	private long amount;
	/**
	 * Last two decimals are cents to avoid floating point errors.
	 */
	private int conversionRate;
}
