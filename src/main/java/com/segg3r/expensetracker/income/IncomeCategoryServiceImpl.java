package com.segg3r.expensetracker.income;

import com.segg3r.expensetracker.exception.InputException;
import com.segg3r.expensetracker.income.model.IncomeCategoryCreationModel;
import com.segg3r.expensetracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeCategoryServiceImpl implements IncomeCategoryService {

	private static final List<IncomeCategoryCreationModel> DEFAULT_USER_MODELS =
			Collections.unmodifiableList(Arrays.asList(
					new IncomeCategoryCreationModel("Salary"),
					new IncomeCategoryCreationModel("Investments")));

	@Autowired
	private IncomeCategoryRepository incomeCategoryRepository;

	@Override
	public Optional<IncomeCategory> getIncomeCategoryById(String id) {
		return incomeCategoryRepository.findById(id);
	}

	@Override
	public void createDefaultIncomeCategories(User user) {
		for (IncomeCategoryCreationModel categoryModel : DEFAULT_USER_MODELS) {
			IncomeCategory category = IncomeCategory.builder()
					.userId(user.getId())
					.name(categoryModel.getName())
					.build();
			createIncomeCategory(category);
		}
	}

	@Override
	public IncomeCategory createIncomeCategory(IncomeCategory category) {
		if (incomeCategoryRepository.findByUserIdAndName(category.getUserId(), category.getName()).isPresent()) {
			throw new InputException("Could not create income category '" + category.getName() + "'. " +
					"Such category already exists.");
		}

		return incomeCategoryRepository.insert(category);
	}

	@Override
	public IncomeCategory editIncomeCategory(IncomeCategory category) {
		Optional<IncomeCategory> existingCategory = incomeCategoryRepository.findByUserIdAndName(category.getUserId(), category.getName());
		if (existingCategory.isPresent() && !existingCategory.get().getId().equals(category.getId())) {
			throw new InputException("Could not edit income category '" + category + "'. " +
					"Category with such name already exists for user " + category.getUserId());
		}

		incomeCategoryRepository.save(category);
		return category;
	}

	@Override
	public IncomeCategory deleteIncomeCategory(String categoryId) {
		IncomeCategory existingCategory = incomeCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new InputException("Could not find income category " + categoryId + " to delete."));

		incomeCategoryRepository.delete(existingCategory);
		return existingCategory;
	}

	@Override
	public void deleteUserIncomeCategories(User user) {
		incomeCategoryRepository.deleteByUserId(user.getId());
	}

	@Override
	public List<IncomeCategory> getUserIncomeCategories(User user) {
		return incomeCategoryRepository.findByUserId(user.getId());
	}

	@Override
	public boolean isUserCategoryOwner(User user, String categoryId) {
		return incomeCategoryRepository.findById(categoryId)
				.filter(existingCategory -> user.getId().equals(existingCategory.getUserId()))
				.isPresent();
	}
}
