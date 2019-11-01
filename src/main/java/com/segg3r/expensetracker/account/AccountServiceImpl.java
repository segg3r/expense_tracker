package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import com.segg3r.expensetracker.account.model.AccountCreationModel;
import com.segg3r.expensetracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

	private static final List<AccountCreationModel> DEFAULT_USER_ACCOUNTS
			= Collections.unmodifiableList(Arrays.asList(
			new AccountCreationModel("Cash", Currency.USD),
			new AccountCreationModel("Debit Card", Currency.USD)));

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> getAccount(String id) {
		return accountRepository.findById(id);
	}

	@Override
	public void createDefaultUserAccounts(User user) throws AccountCreationException {
		for (AccountCreationModel accountMetadata : DEFAULT_USER_ACCOUNTS) {
			Account currency = Account.builder()
					.userId(user.getId())
					.amount(0)
					.name(accountMetadata.getName())
					.currency(accountMetadata.getCurrency())
					.build();
			createAccount(currency);
		}
	}

	@Override
	public Account createAccount(@Valid Account account) throws AccountCreationException {
		if (accountRepository.findByUserIdAndName(account.getUserId(), account.getName()).isPresent()) {
			throw new AccountCreationException("Could not create account '" + account.getName() + "'. " +
					"Such account already exists.");
		}

		return accountRepository.insert(account);
	}

	@Override
	public void editAccount(@Valid Account account) throws AccountEditException {
		Optional<Account> existingAccount = accountRepository.findByUserIdAndName(account.getUserId(), account.getName());
		if (existingAccount.isPresent() && !existingAccount.get().getId().equals(account.getId())) {
			throw new AccountEditException("Could not edit account '" + account.getName() + "'. " +
					"Account with such name already exists for user " + account.getUserId() + ".");
		}

		accountRepository.save(account);
	}

	@Override
	public void deleteAccount(Account account) {
		accountRepository.delete(account);
	}
}
