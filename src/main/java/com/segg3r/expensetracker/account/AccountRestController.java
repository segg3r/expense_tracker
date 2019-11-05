package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountDeletionException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import com.segg3r.expensetracker.account.model.AccountCreationModel;
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
@RequestMapping("/api/accounts")
public class AccountRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Account createAccount(@Valid @RequestBody AccountCreationModel accountCreation) {
		try {
			User user = securityContext.getCurrentUser();

			log.info("Trying to create account " + accountCreation + " for user " + user.getName() + ".");
			Account account = Account.builder()
					.name(accountCreation.getName())
					.userId(user.getId())
					.currency(accountCreation.getCurrency())
					.amount(0)
					.build();

			Account createdAccount = accountService.createAccount(account);
			log.info("Successfully created account " + createdAccount.getId() + ".");
			return createdAccount;
		} catch (AccountCreationException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(path = "/{accountId}", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Account editAccount(@PathVariable String accountId, @Valid @RequestBody Account account) {
		validateCurrentUserIsAccountOwner(accountId);

		try {
			log.info("Trying to edit account " + accountId + ".");

			Account editedAccount = accountService.editAccount(account);
			log.info("Successfully edited account " + accountId + ".");
			return editedAccount;
		} catch (AccountEditException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(path = "/{accountId}", method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Account deleteAccount(@PathVariable String accountId) {
		validateCurrentUserIsAccountOwner(accountId);

		try {
			log.info("Trying to edit account '" + accountId + "'.");
			Account deletedAccount = accountService.deleteAccount(accountId);
			log.info("Successfully deleted account " + accountId + ".");
			return deletedAccount;
		} catch (AccountDeletionException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(path = "/{accountId}", method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Account getAccountById(@PathVariable String accountId) {
		validateCurrentUserIsAccountOwner(accountId);
		return accountService.getAccount(accountId)
				.orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Could not find account " + accountId + "."));
	}

	@RequestMapping(method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Account> getUserAccounts() {
		User user = securityContext.getCurrentUser();
		return accountService.getUserAccounts(user);
	}

	private void validateCurrentUserIsAccountOwner(String accountId) {
		User user = securityContext.getCurrentUser();
		if (!accountService.isUserAccountOwner(user, accountId)) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Current user does not have permission for the account.");
		}
	}

}
