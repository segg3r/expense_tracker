package com.segg3r.expensetracker.account;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class AccountMoneyTransfer {

	@Id
	private String id;
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
