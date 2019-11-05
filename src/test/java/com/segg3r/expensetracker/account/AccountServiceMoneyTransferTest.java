package com.segg3r.expensetracker.account;

import com.segg3r.expensetracker.MockitoTest;
import com.segg3r.expensetracker.account.exception.AccountMoneyTransferException;
import com.segg3r.expensetracker.security.SecurityContext;
import com.segg3r.expensetracker.user.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class AccountServiceMoneyTransferTest extends MockitoTest {

	private static final String USER_ID = "userId";
	private static final String FROM_ACCOUNT_ID = "fromAccountId";
	private static final String TO_ACCOUNT_ID = "toAccountId";
	private static final int DEFAULT_CONVERSION_RATE = 100;
	private static final int DEFAULT_AMOUNT = 100;

	@Mock
	private SecurityContext securityContext;
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private AccountMoneyTransferRepository accountMoneyTransferRepository;
	@InjectMocks
	private AccountServiceImpl accountService;

	private Account fromAccount;
	private Account toAccount;
	private AccountMoneyTransfer transfer;

	@BeforeMethod
	public void prepare() {
		givenUser();
		givenFromAccount();
		givenToAccount();
		givenTransfer();
	}

	@Test(description = "should successfully execute money transfer")
	public void testTransferBetweenAccounts_Success() throws AccountMoneyTransferException {
		performTransfer();
		verifySuccessfulTransfer(0, 200);
	}

	@Test(description = "should fail if from account was not found",
		expectedExceptions = AccountMoneyTransferException.class)
	public void testTransferBetweenAccounts_WrongFromAccount() throws AccountMoneyTransferException {
		transfer.setFromAccountId("unknown");
		performTransfer();
	}

	@Test(description = "should fail if to account was not found",
			expectedExceptions = AccountMoneyTransferException.class)
	public void testTransferBetweenAccounts_WrongToAccount() throws AccountMoneyTransferException {
		transfer.setToAccountId("unknown");
		performTransfer();
	}

	@Test(description = "should fail if from account does not belong to user",
			expectedExceptions = AccountMoneyTransferException.class)
	public void testTransferBetweenAccounts_FromAccountHasWrongUser() throws AccountMoneyTransferException {
		fromAccount.setUserId("unknown");
		performTransfer();
	}

	@Test(description = "should fail if to account does not belong to user",
			expectedExceptions = AccountMoneyTransferException.class)
	public void testTransferBetweenAccounts_ToAccountHasWrongUser() throws AccountMoneyTransferException {
		toAccount.setUserId("unknown");
		performTransfer();
	}

	@Test(description = "should fail if from account does not have enough money",
			expectedExceptions = AccountMoneyTransferException.class)
	public void testTransferBetweenAccounts_NotEnoughMoney() throws AccountMoneyTransferException {
		fromAccount.setAmount(99);
		performTransfer();
	}

	private void performTransfer() throws AccountMoneyTransferException {
		accountService.transferBetweenAccounts(transfer);
	}

	private void verifySuccessfulTransfer(long expectedFromAmount, long expectedToAmount) {
		verify(accountRepository).save(eq(fromAccount));
		verify(accountRepository).save(eq(toAccount));
		verify(accountMoneyTransferRepository).save(eq(transfer));

		assertEquals(fromAccount.getAmount(), expectedFromAmount);
		assertEquals(toAccount.getAmount(), expectedToAmount);
	}

	private void givenTransfer() {
		this.transfer = AccountMoneyTransfer.builder()
				.fromAccountId(FROM_ACCOUNT_ID)
				.toAccountId(TO_ACCOUNT_ID)
				.amount(DEFAULT_AMOUNT)
				.conversionRate(DEFAULT_CONVERSION_RATE)
				.build();
	}

	private void givenFromAccount() {
		this.fromAccount = givenAccount(FROM_ACCOUNT_ID);
	}

	private void givenToAccount() {
		this.toAccount = givenAccount(TO_ACCOUNT_ID);
	}

	private void givenUser() {
		User user = User.builder()
				.id(USER_ID)
				.build();

		when(securityContext.getCurrentUser()).thenReturn(user);
	}

	private Account givenAccount(String accountId) {
		Account account = Account.builder()
				.id(accountId)
				.amount(DEFAULT_AMOUNT)
				.userId(USER_ID)
				.currency(Currency.USD)
				.build();

		when(accountRepository.findById(eq(accountId)))
				.thenReturn(Optional.of(account));

		return account;
	}



}