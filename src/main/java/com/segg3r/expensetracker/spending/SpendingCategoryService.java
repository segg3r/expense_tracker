package com.segg3r.expensetracker.spending;

import com.segg3r.expensetracker.user.User;

import java.util.List;
import java.util.Optional;

public interface SpendingCategoryService {
	Optional<SpendingCategory> getAccount(String id);
	void createDefaultSpendingCategories(User user);
	SpendingCategory createSpendingCategory(SpendingCategory category);
	SpendingCategory editSpendingCategory(SpendingCategory category);
	SpendingCategory deleteSpendingCategory(String categoryId);
	void deleteUserSpendingCategories(User user);
	List<SpendingCategory> getUserSpendingCategories(User user);
	boolean isUserCategoryOwner(User user, String categoryId);
}
