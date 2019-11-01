package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import com.segg3r.expensetracker.account.exception.AccountMoneyTransferException;
import com.segg3r.expensetracker.user.User;

import java.util.Optional;

public interface AccountService {
	Optional<Account> getAccount(String id);
	void createDefaultUserAccounts(User user) throws AccountCreationException;
	Account createAccount(Account account) throws AccountCreationException;
	void editAccount(Account account) throws AccountEditException;
	void deleteAccount(Account account);
	void deleteUserAccounts(User user);
	void transferBetweenAccounts(AccountMoneyTransfer accountMoneyTransfer) throws AccountMoneyTransferException;
}
