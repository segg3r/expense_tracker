package com.segg3r.expensetracker.user;

import com.segg3r.expensetracker.account.AccountService;
import com.segg3r.expensetracker.exception.InternalException;
import com.segg3r.expensetracker.income.IncomeCategoryService;
import com.segg3r.expensetracker.security.UsernamePassword;
import com.segg3r.expensetracker.spending.SpendingCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private IncomeCategoryService incomeCategoryService;
	@Autowired
	private SpendingCategoryService spendingCategoryService;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	void initAdmin() {
		userRepository.findByName("admin").ifPresent(this::unregisterUser);

		UsernamePassword usernamePassword = new UsernamePassword("admin", "admin");
		this.registerUser(usernamePassword);
	}

	@Transactional
	@Override
	public User registerUser(UsernamePassword usernamePassword) {
		String username = usernamePassword.getUsername();
		log.info("Attempting to register user '" + username + "'.");

		userService.createUser(usernamePassword);

		User createdUser = userService.findByName(usernamePassword.getUsername())
				.orElseThrow(() -> new InternalException("Could not find created user."));

		accountService.createDefaultUserAccounts(createdUser);
		incomeCategoryService.createDefaultIncomeCategories(createdUser);
		spendingCategoryService.createDefaultSpendingCategories(createdUser);

		log.info("Successfully registered user '" + username + "'.");
		return createdUser;
	}

	@Override
	public void unregisterUser(User user) {
		log.info("Attempting to unregister user '" + user.getName() + "'.");

		accountService.deleteUserAccounts(user);
		incomeCategoryService.deleteUserIncomeCategories(user);
		spendingCategoryService.deleteUserSpendingCategories(user);
		userService.deleteUser(user);

		log.info("Successfully unregistered user '" + user.getName() + "'.");
	}

}
