package com.segg3r.expensetracker.income;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class IncomeCategory {

	@Id
	private String id;
	@NotNull(message = "User is not specified for income category.")
	private String userId;
	@NotNull(message = "Income category name is not specified.")
	private String name;

}
