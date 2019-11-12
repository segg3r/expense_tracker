package com.segg3r.expensetracker.spending;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class SpendingCategory {

	@Id
	private String id;
	@NotNull(message = "User is not specified for spending category.")
	private String userId;
	@NotNull(message = "Spending category name is not specified.")
	private String name;

}
