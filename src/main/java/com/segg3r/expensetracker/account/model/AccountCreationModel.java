package com.segg3r.expensetracker.account.model;

import com.segg3r.expensetracker.account.Currency;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class AccountCreationModel {
	@NotNull(message = "Name is not specified for account.")
	private String name;
	@NotNull(message = "Current is not specified for account.")
	private Currency currency;
}
