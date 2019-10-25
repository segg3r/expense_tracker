package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.model.AccountCreationModel;
import com.segg3r.expensetracker.security.SecurityContext;
import com.segg3r.expensetracker.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;

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

			return accountService.createAccount(account);
		} catch (AccountCreationException e) {
			log.warn(e.getMessage(), e);
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
