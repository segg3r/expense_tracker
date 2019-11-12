package com.segg3r.expensetracker.accountmoneytransfer;

import com.segg3r.expensetracker.account.AccountService;
import com.segg3r.expensetracker.exception.PermissionException;
import com.segg3r.expensetracker.security.SecurityContext;
import com.segg3r.expensetracker.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/account_money_transfers")
public class AccountMoneyTransferRestController {

	@Autowired
	private SecurityContext securityContext;
	@Autowired
	private AccountService accountService;

	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AccountMoneyTransfer transferMoneyBetweenAccounts(@Valid @RequestBody AccountMoneyTransfer transfer) {
		validateAccountsBelongToCurrentUser(transfer);
		return accountService.transferBetweenAccounts(transfer);
	}

	private void validateAccountsBelongToCurrentUser(AccountMoneyTransfer transfer) {
		validateAccountBelongsToCurrentUser(transfer.getFromAccountId());
		validateAccountBelongsToCurrentUser(transfer.getToAccountId());
	}

	private void validateAccountBelongsToCurrentUser(String accountId) {
		User user = securityContext.getCurrentUser();

		Optional.ofNullable(accountId)
				.flatMap(accountService::getAccount)
				.filter(fromAccount -> fromAccount.getUserId().equals(user.getId()))
				.orElseThrow(() -> new PermissionException("Current user is not an owner of the account."));
	}

}
