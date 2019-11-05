package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountDeletionException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import com.segg3r.expensetracker.accountmoneytransfer.exception.AccountMoneyTransferException;
import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransfer;
import com.segg3r.expensetracker.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {
	Optional<Account> getAccount(String id);
	void createDefaultUserAccounts(User user) throws AccountCreationException;
	Account createAccount(Account account) throws AccountCreationException;
	Account editAccount(Account account) throws AccountEditException;
	Account deleteAccount(String accountId) throws AccountDeletionException;
	void deleteUserAccounts(User user);
	AccountMoneyTransfer transferBetweenAccounts(AccountMoneyTransfer accountMoneyTransfer) throws AccountMoneyTransferException;
	List<Account> getUserAccounts(User user);
	boolean isUserAccountOwner(User user, String accountId);
}
