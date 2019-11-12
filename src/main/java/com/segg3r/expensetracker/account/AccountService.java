package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransfer;
import com.segg3r.expensetracker.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {
	Optional<Account> getAccount(String id);
	void createDefaultUserAccounts(User user);
	Account createAccount(Account account);
	Account editAccount(Account account);
	Account deleteAccount(String accountId);
	void deleteUserAccounts(User user);
	AccountMoneyTransfer transferBetweenAccounts(AccountMoneyTransfer accountMoneyTransfer);
	List<Account> getUserAccounts(User user);
	boolean isUserAccountOwner(User user, String accountId);
}
