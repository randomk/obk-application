package com.banking.account.service;

import com.banking.account.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountServiceTest {

    @Mock
    private AccountRepository mockAccountRepository;

    private AccountService accountServiceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        accountServiceUnderTest = new AccountService(mockAccountRepository);
    }

    @Test
    public void testCreateAccount() {

        final UUID expected = UUID.randomUUID();
        doReturn(expected).when(mockAccountRepository).createAccount();
        // Run the test
        final UUID result = accountServiceUnderTest.createAccount();

        // Verify the results
        assertEquals(expected, result);
    }

    @Test
    public void testGetAccountBalance() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal expectedResult = new BigDecimal("0.00");
        doReturn(expectedResult).when(mockAccountRepository).getAccountBalance(any(UUID.class));
        // Run the test
        final BigDecimal result = accountServiceUnderTest.getAccountBalance(accountId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsAccountPresent() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final boolean expectedResult = false;
        doReturn(expectedResult).when(mockAccountRepository).isAccountPresent(any(UUID.class));
        // Run the test
        final boolean result = accountServiceUnderTest.isAccountPresent(accountId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDebitToAccountBalance() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal amount = new BigDecimal("0.00");

        // Run the test
        accountServiceUnderTest.debitToAccountBalance(accountId, amount);

        // Verify the results
    }

    @Test
    public void testCreditToAccountBalance() {
        // Setup
        final UUID accountId = UUID.randomUUID();
        final BigDecimal amount = new BigDecimal("0.00");

        // Run the test
        accountServiceUnderTest.creditToAccountBalance(accountId, amount);

        // Verify the results
    }
}
