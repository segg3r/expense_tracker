package com.segg3r.expensetracker.spending.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendingCategoryCreationModel {

	@NotNull(message = "Spending category name is not specified.")
	private String name;

}
