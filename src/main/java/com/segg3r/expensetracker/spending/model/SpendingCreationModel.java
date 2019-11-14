package com.segg3r.expensetracker.spending.model;

import com.segg3r.expensetracker.account.Currency;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SpendingCreationModel {

	@NotNull(message = "Spending category is not specified for spending.")
	private String spendingCategoryId;
	@NotNull(message = "Current is not specified for the spending.")
	private Currency currency;
	@Min(value = 1, message = "Amount of money spent should be greater than 0.")
	private long amount;

}
