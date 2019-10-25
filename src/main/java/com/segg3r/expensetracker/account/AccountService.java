package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface AccountService {
	Optional<Account> getAccount(ObjectId id);
	@javax.validation.Valid Account createAccount(Account account) throws AccountCreationException;
	void editAccount(Account account) throws AccountEditException;
	void deleteAccount(Account account);
}
