package com.segg3r.expensetracker.income.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeCategoryCreationModel {

	@NotNull(message = "Spending category name is not specified.")
	private String name;

}
