package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.exception.AccountCreationException;
import com.segg3r.expensetracker.account.exception.AccountDeletionException;
import com.segg3r.expensetracker.account.exception.AccountEditException;
import com.segg3r.expensetracker.accountmoneytransfer.exception.AccountMoneyTransferException;
import com.segg3r.expensetracker.account.model.AccountCreationModel;
import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransfer;
import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransferRepository;
import com.segg3r.expensetracker.security.SecurityContext;
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

	private static final double CONVERSION_MULTIPLIER = 0.01;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountMoneyTransferRepository accountMoneyTransferRepository;
	@Autowired
	private SecurityContext securityContext;

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
	public Account editAccount(@Valid Account account) throws AccountEditException {
		Optional<Account> existingAccount = accountRepository.findByUserIdAndName(account.getUserId(), account.getName());
		if (existingAccount.isPresent() && !existingAccount.get().getId().equals(account.getId())) {
			throw new AccountEditException("Could not edit account '" + account.getName() + "'. " +
					"Account with such name already exists for user " + account.getUserId() + ".");
		}

		accountRepository.save(account);
		return account;
	}

	@Override
	public Account deleteAccount(String accountId) throws AccountDeletionException {
		Account existingAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountDeletionException("Could not find account " + accountId + " to delete."));

		accountRepository.delete(existingAccount);
		return existingAccount;
	}

	@Override
	public void deleteUserAccounts(User user) {
		accountRepository.deleteByUserId(user.getId());
	}

	@Override
	public AccountMoneyTransfer transferBetweenAccounts(AccountMoneyTransfer transfer) throws AccountMoneyTransferException {
		validateAccountMoneyTransfer(transfer);
		return performAccountMoneyTransfer(transfer);
	}

	@Override
	public List<Account> getUserAccounts(User user) {
		return accountRepository.findByUserId(user.getId());
	}

	@Override
	public boolean isUserAccountOwner(User user, String accountId) {
		return accountRepository.findById(accountId)
				.filter(existingAccount -> user.getId().equals(existingAccount.getUserId()))
				.isPresent();
	}

	private void validateAccountMoneyTransfer(AccountMoneyTransfer transfer) throws AccountMoneyTransferException {
		Account from = getTransferFromAccount(transfer);
		validateAccountHaveEnoughMoney(from, transfer.getAmount());
	}

	private void validateAccountHaveEnoughMoney(Account from, long amount) throws AccountMoneyTransferException {
		if (from.getAmount() < amount) {
			throw new AccountMoneyTransferException("Account '" + from.getName() + "' does not have enough currency.");
		}
	}

	private AccountMoneyTransfer performAccountMoneyTransfer(AccountMoneyTransfer transfer) throws AccountMoneyTransferException {
		Account from = getTransferFromAccount(transfer);
		from.subtractMoney(transfer.getAmount());
		accountRepository.save(from);

		Account to = getTransferToAccount(transfer);
		to.addMoney((long)(transfer.getAmount() * transfer.getConversionRate() * CONVERSION_MULTIPLIER));
		accountRepository.save(to);

		return accountMoneyTransferRepository.save(transfer);
	}

	private Account getTransferFromAccount(AccountMoneyTransfer accountMoneyTransfer) throws AccountMoneyTransferException {
		return accountRepository.findById(accountMoneyTransfer.getFromAccountId())
				.orElseThrow(() -> new AccountMoneyTransferException("Could not find account to transfer money from."));
	}

	private Account getTransferToAccount(AccountMoneyTransfer accountMoneyTransfer) throws AccountMoneyTransferException {
		return accountRepository.findById(accountMoneyTransfer.getToAccountId())
				.orElseThrow(() -> new AccountMoneyTransferException("Could not find account to transfer money to."));
	}

}
