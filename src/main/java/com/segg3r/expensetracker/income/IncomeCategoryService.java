package com.segg3r.expensetracker.income;

import com.segg3r.expensetracker.user.User;

import java.util.List;
import java.util.Optional;

public interface IncomeCategoryService {
	Optional<IncomeCategory> getIncomeCategoryById(String id);
	void createDefaultIncomeCategories(User user);
	IncomeCategory createIncomeCategory(IncomeCategory category);
	IncomeCategory editIncomeCategory(IncomeCategory category);
	IncomeCategory deleteIncomeCategory(String categoryId);
	void deleteUserIncomeCategories(User user);
	List<IncomeCategory> getUserIncomeCategories(User user);
	boolean isUserCategoryOwner(User user, String categoryId);
}
