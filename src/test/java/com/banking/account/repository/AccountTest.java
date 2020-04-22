package com.banking.account.repository;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;

public class AccountTest {

    private Account accountUnderTest;

    @Before
    public void setUp() {
        accountUnderTest = new Account();
    }

    @Test
    public void testCredit() {
        // Setup
        final BigDecimal amount = new BigDecimal("50.00");

        // Run the test
        accountUnderTest.credit(amount);

        // Verify the results
        assertTrue(accountUnderTest.getBalance().equals(amount));
    }

    @Test
    public void testDebit() {
        // Setup
        final BigDecimal amount = new BigDecimal("50.00");

        // Run the test
        accountUnderTest.debit(amount);

        // Verify the results
        assertTrue(accountUnderTest.getBalance().equals(amount.negate()));

    }
}
