package com.segg3r.expensetracker.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Account {

	@Id
	private String id;
	@NotNull(message = "Account user is not specified.")
	private String userId;
	@NotNull(message = "Account name is not specified.")
	private String name;
	/**
	 * Last two decimals are cents to avoid floating point errors.
	 */
	@Min(value = 0, message = "Amount of money on account cannot be less than 0.")
	private long amount;
	@NotNull(message = "Account currency is not specified.")
	private Currency currency;

	void subtractMoney(long amount) {
		this.amount -= amount;
	}

	void addMoney(long amount) {
		this.amount += amount;
	}

}
