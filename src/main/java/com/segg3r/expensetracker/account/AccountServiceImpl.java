package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.account.model.AccountCreationModel;
import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransfer;
import com.segg3r.expensetracker.accountmoneytransfer.AccountMoneyTransferRepository;
import com.segg3r.expensetracker.exception.InputException;
import com.segg3r.expensetracker.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.*;

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

	@Override
	public Optional<Account> getAccount(String id) {
		return accountRepository.findById(id);
	}

	@Override
	public void createDefaultUserAccounts(User user) {
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
	public Account createAccount(@Valid Account account) {
		if (accountRepository.findByUserIdAndName(account.getUserId(), account.getName()).isPresent()) {
			throw new InputException( "Could not create account '" + account.getName() + "'. " +
					"Such account already exists.");
		}

		return accountRepository.insert(account);
	}

	@Override
	public Account editAccount(@Valid Account account) {
		Optional<Account> existingAccount = accountRepository.findByUserIdAndName(account.getUserId(), account.getName());
		if (existingAccount.isPresent() && !existingAccount.get().getId().equals(account.getId())) {
			throw new InputException("Could not edit account '" + account.getName() + "'. " +
					"Account with such name already exists for user " + account.getUserId() + ".");
		}

		accountRepository.save(account);
		return account;
	}

	@Override
	public Account deleteAccount(String accountId) {
		Account existingAccount = accountRepository.findById(accountId)
				.orElseThrow(() -> new InputException("Could not find account " + accountId + " to delete."));

		accountRepository.delete(existingAccount);
		return existingAccount;
	}

	@Override
	public void deleteUserAccounts(User user) {
		accountRepository.deleteByUserId(user.getId());
	}

	@Transactional
	@Override
	public AccountMoneyTransfer transferBetweenAccounts(AccountMoneyTransfer transfer) {
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

	private void validateAccountMoneyTransfer(AccountMoneyTransfer transfer) {
		Account from = getTransferFromAccount(transfer);
		validateAccountHaveEnoughMoney(from, transfer.getAmount());
	}

	private void validateAccountHaveEnoughMoney(Account from, long amount) {
		if (from.getAmount() < amount) {
			throw new InputException("Account '" + from.getName() + "' does not have enough currency.");
		}
	}

	private AccountMoneyTransfer performAccountMoneyTransfer(AccountMoneyTransfer transfer) {
		transfer.setDate(new Date());

		Account from = getTransferFromAccount(transfer);
		from.subtractMoney(transfer.getAmount());
		accountRepository.save(from);

		Account to = getTransferToAccount(transfer);
		to.addMoney((long)(transfer.getAmount() * transfer.getConversionRate() * CONVERSION_MULTIPLIER));
		accountRepository.save(to);

		return accountMoneyTransferRepository.save(transfer);
	}

	private Account getTransferFromAccount(AccountMoneyTransfer accountMoneyTransfer) {
		return accountRepository.findById(accountMoneyTransfer.getFromAccountId())
				.orElseThrow(() -> new InputException("Could not find account to transfer money from."));
	}

	private Account getTransferToAccount(AccountMoneyTransfer accountMoneyTransfer) {
		return accountRepository.findById(accountMoneyTransfer.getToAccountId())
				.orElseThrow(() -> new InputException("Could not find account to transfer money to."));
	}

}
