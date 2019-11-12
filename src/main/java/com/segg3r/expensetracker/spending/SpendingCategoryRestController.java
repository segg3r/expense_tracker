package com.segg3r.expensetracker.spending;

import com.segg3r.expensetracker.security.SecurityContext;
import com.segg3r.expensetracker.spending.model.SpendingCategoryCreationModel;
import com.segg3r.expensetracker.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/spending_categories")
public class SpendingCategoryRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private SpendingCategoryService spendingCategoryService;

	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SpendingCategory createSpendingCategory(@Valid @RequestBody SpendingCategoryCreationModel categoryModel) {
		User user = securityContext.getCurrentUser();

		log.info("Trying to create spending category " + categoryModel + " for user " + user.getName() + ".");
		SpendingCategory spendingCategory = SpendingCategory.builder()
				.name(categoryModel.getName())
				.userId(user.getId())
				.build();

		SpendingCategory createdSpendingCategory = spendingCategoryService.createSpendingCategory(spendingCategory);
		log.info("Successfully created account " + createdSpendingCategory.getId() + ".");
		return createdSpendingCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SpendingCategory editSpendingCategory(@PathVariable String categoryId, @Valid @RequestBody SpendingCategory category) {
		validateCurrentUserIsCategoryOwner(categoryId);

		log.info("Trying to edit account " + categoryId + ".");

		SpendingCategory editedCategory = spendingCategoryService.editSpendingCategory(category);
		log.info("Successfully edited account " + categoryId + ".");
		return editedCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SpendingCategory deleteSpendingCategory(@PathVariable String categoryId) {
		validateCurrentUserIsCategoryOwner(categoryId);

		log.info("Trying to edit account '" + categoryId + "'.");
		SpendingCategory deletedSpendingCategory = spendingCategoryService.deleteSpendingCategory(categoryId);
		log.info("Successfully deleted account " + categoryId + ".");
		return deletedSpendingCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SpendingCategory getSpendingCategoryById(@PathVariable String categoryId) {
		validateCurrentUserIsCategoryOwner(categoryId);
		return spendingCategoryService.getAccount(categoryId)
				.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Could not find category " + categoryId + "."));
	}

	@RequestMapping(method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpendingCategory> getUserSpendingCategories() {
		User user = securityContext.getCurrentUser();
		return spendingCategoryService.getUserSpendingCategories(user);
	}

	private void validateCurrentUserIsCategoryOwner(String categoryId) {
		User user = securityContext.getCurrentUser();
		if (!spendingCategoryService.isUserCategoryOwner(user, categoryId)) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Current user does not have permission for the account.");
		}
	}

}
