package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> getAccount(ObjectId id) {
		return accountRepository.findById(id);
	}

	@Override
	public Account createAccount(@Valid Account account) throws AccountCreationException {
		if (accountRepository.findByUserIdAndName(account.getUserId(), account.getName()).isPresent()) {
			throw new AccountCreationException("Could not create account '" + account.getName() + "'. " +
					"Such account already exists for user " + account.getUserId() + ".");
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
