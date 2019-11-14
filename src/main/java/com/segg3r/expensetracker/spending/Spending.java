package com.segg3r.expensetracker.spending;

import com.segg3r.expensetracker.account.Currency;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
public class Spending {

	@Id
	private String id;
	@NotNull(message = "User is not specified for spending.")
	private String userId;
	@NotNull(message = "Spending category is not specified for spending.")
	private String spendingCategoryId;
	@NotNull(message = "Current is not specified for the spending.")
	private Currency currency;
	@Min(value = 1, message = "Amount of money spent should be greater than 0.")
	private long amount;
	private Date date;

}
