package com.banking.account.repository;

import com.banking.account.exception.AccountNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountRepositoryTest {

    private AccountRepository accountRepositoryUnderTest;

    @Before
    public void setUp() {
        accountRepositoryUnderTest = new AccountRepository();
    }

    @Test
    public void testCreateAccount() {

        // Run the test
        final UUID result = accountRepositoryUnderTest.createAccount();

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testGetAccountBalanceWhenAccountExists() {
        // Setup
        final UUID account = accountRepositoryUnderTest.createAccount();
        // Run the test
        final BigDecimal result = accountRepositoryUnderTest.getAccountBalance(account);
        // Verify the results
        assertTrue(result.equals(BigDecimal.ZERO));
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetAccountBalanceWhenAccountDoesNotExists() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal expectedResult = new BigDecimal("0.00");

        // Run the test
        final BigDecimal result = accountRepositoryUnderTest.getAccountBalance(accountId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsAccountPresent() {
        // Setup
        final UUID accountID = UUID.randomUUID();
        final boolean expectedResult = false;

        // Run the test
        final boolean result = accountRepositoryUnderTest.isAccountPresent(accountID);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDebitToAccountBalanceWhenAccountNotPresent() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal amount = new BigDecimal("0.00");

        // Run the test
        accountRepositoryUnderTest.debitToAccountBalance(accountId, amount);

        // Verify the results
    }

    @Test
    public void testDebitToAccountBalanceWhenAccountIsPresent() {
        // Setup
        final UUID accountId = accountRepositoryUnderTest.createAccount();
        final BigDecimal amount = new BigDecimal("0.00");
        // Run the test
        accountRepositoryUnderTest.debitToAccountBalance(accountId, amount);

        // Verify the results
    }

    @Test(expected = AccountNotFoundException.class)
    public void testCreditToAccountBalanceWhenAccountNotPresent() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal amount = new BigDecimal("0.00");

        // Run the test
        accountRepositoryUnderTest.creditToAccountBalance(accountId, amount);

        // Verify the results
    }

    @Test
    public void testCreditToAccountBalanceWhenAccountIsPresent() {
        // Setup
        final UUID accountId = accountRepositoryUnderTest.createAccount();
        final BigDecimal amount = new BigDecimal("0.00");

        // Run the test
        accountRepositoryUnderTest.creditToAccountBalance(accountId, amount);

        // Verify the results
    }
}
