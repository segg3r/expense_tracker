package com.segg3r.expensetracker.spending;

import com.segg3r.expensetracker.exception.InputException;
import com.segg3r.expensetracker.spending.model.SpendingCategoryCreationModel;
import com.segg3r.expensetracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SpendingCategoryServiceImpl implements SpendingCategoryService {

	private static final List<SpendingCategoryCreationModel> DEFAULT_USER_MODELS =
			Collections.unmodifiableList(Arrays.asList(
					new SpendingCategoryCreationModel("Utilities"),
					new SpendingCategoryCreationModel("Public Transport")));

	@Autowired
	private SpendingCategoryRepository spendingCategoryRepository;

	@Override
	public Optional<SpendingCategory> getAccount(String id) {
		return spendingCategoryRepository.findById(id);
	}

	@Override
	public void createDefaultSpendingCategories(User user) {
		for (SpendingCategoryCreationModel categoryModel : DEFAULT_USER_MODELS) {
			SpendingCategory category = SpendingCategory.builder()
					.userId(user.getId())
					.name(categoryModel.getName())
					.build();
			createSpendingCategory(category);
		}
	}

	@Override
	public SpendingCategory createSpendingCategory(@Valid SpendingCategory category) {
		if (spendingCategoryRepository.findByUserIdAndName(category.getUserId(), category.getName()).isPresent()) {
			throw new InputException("Could not create spending category '" + category.getName() + "'. " +
					"Such category already exists.");
		}

		return spendingCategoryRepository.insert(category);
	}

	@Override
	public SpendingCategory editSpendingCategory(@Valid SpendingCategory category) {
		Optional<SpendingCategory> existingCategory = spendingCategoryRepository.findByUserIdAndName(category.getUserId(), category.getName());
		if (existingCategory.isPresent() && !existingCategory.get().getId().equals(category.getId())) {
			throw new InputException("Could not edit spending category '" + category + "'. " +
					"Category with such name already exists for user " + category.getUserId());
		}

		spendingCategoryRepository.save(category);
		return category;
	}

	@Override
	public SpendingCategory deleteSpendingCategory(String categoryId) {
		SpendingCategory existingCategory = spendingCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new InputException("Could not find spending category " + categoryId + " to delete."));

		spendingCategoryRepository.delete(existingCategory);
		return existingCategory;
	}

	@Override
	public void deleteUserSpendingCategories(User user) {
		spendingCategoryRepository.deleteByUserId(user.getId());
	}

	@Override
	public List<SpendingCategory> getUserSpendingCategories(User user) {
		return spendingCategoryRepository.findByUserId(user.getId());
	}

	@Override
	public boolean isUserCategoryOwner(User user, String categoryId) {
		return spendingCategoryRepository.findById(categoryId)
				.filter(existingCategory -> user.getId().equals(existingCategory.getUserId()))
				.isPresent();
	}

}
