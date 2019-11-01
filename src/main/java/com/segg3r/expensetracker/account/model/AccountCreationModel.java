package com.segg3r.expensetracker.account.model;

import com.segg3r.expensetracker.account.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationModel {
	@NotNull(message = "Name is not specified for account.")
	private String name;
	@NotNull(message = "Current is not specified for account.")
	private Currency currency;
}
