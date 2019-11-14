package com.segg3r.expensetracker.income;

import com.segg3r.expensetracker.exception.InputException;
import com.segg3r.expensetracker.income.model.IncomeCategoryCreationModel;
import com.segg3r.expensetracker.security.SecurityContext;
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
@RequestMapping("/api/income_categories")
public class IncomeCategoryRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private IncomeCategoryService incomeCategoryService;

	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IncomeCategory createIncomeCategory(@Valid @RequestBody IncomeCategoryCreationModel categoryModel) {
		User user = securityContext.getCurrentUser();

		log.info("Trying to create spending category " + categoryModel + " for user " + user.getName() + ".");
		IncomeCategory incomeCategory = IncomeCategory.builder()
				.name(categoryModel.getName())
				.userId(user.getId())
				.build();

		IncomeCategory createdIncomeCategory = incomeCategoryService.createIncomeCategory(incomeCategory);
		log.info("Successfully created income category " + createdIncomeCategory.getId() + ".");
		return createdIncomeCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IncomeCategory editIncomeCategory(@PathVariable String categoryId, @Valid @RequestBody IncomeCategory category) {
		validateCurrentUserIsCategoryOwner(categoryId);

		log.info("Trying to edit income category " + categoryId + ".");

		IncomeCategory editedCategory = incomeCategoryService.editIncomeCategory(category);
		log.info("Successfully edited income category " + categoryId + ".");
		return editedCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IncomeCategory deleteIncomeCategory(@PathVariable String categoryId) {
		validateCurrentUserIsCategoryOwner(categoryId);

		log.info("Trying to edit income category '" + categoryId + "'.");
		IncomeCategory deleteIncomeCategory = incomeCategoryService.deleteIncomeCategory(categoryId);
		log.info("Successfully deleted income category " + categoryId + ".");
		return deleteIncomeCategory;
	}

	@RequestMapping(path = "/{categoryId}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public IncomeCategory getIncomeCategoryById(@PathVariable String categoryId) {
		validateCurrentUserIsCategoryOwner(categoryId);
		return incomeCategoryService.getIncomeCategoryById(categoryId)
				.orElseThrow(() -> new InputException("Could not find category " + categoryId + "."));
	}

	@RequestMapping(method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncomeCategory> getUserIncomeCategories() {
		User user = securityContext.getCurrentUser();
		return incomeCategoryService.getUserIncomeCategories(user);
	}

	private void validateCurrentUserIsCategoryOwner(String categoryId) {
		User user = securityContext.getCurrentUser();
		if (!incomeCategoryService.isUserCategoryOwner(user, categoryId)) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Current user does not have permission for the income category.");
		}
	}

}
