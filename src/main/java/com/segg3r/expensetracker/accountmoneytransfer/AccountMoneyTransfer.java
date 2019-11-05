package com.segg3r.expensetracker.accountmoneytransfer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Document
public class AccountMoneyTransfer {

	@Id
	private String id;
	@NotNull(message = "Account to transfer money from is not specified.")
	private String fromAccountId;
	@NotNull(message = "Account to transfer money to is not specified.")
	private String toAccountId;
	/**
	 * Last two decimals are cents to avoid floating point errors.
	 */
	@Min(value = 1, message = "Amount of transferred money should be positive.")
	private long amount;
	/**
	 * Last two decimals are cents to avoid floating point errors.
	 */
	@Min(value = 0, message = "Conversion rate should be positive.")
	private int conversionRate;

}
