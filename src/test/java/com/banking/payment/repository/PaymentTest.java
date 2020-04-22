package com.banking.payment.repository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class PaymentTest {

    private String message;

    private PaymentStatus status;

    private Payment paymentUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        message = "initialMessage";
        status = null;
        paymentUnderTest = new Payment(message, status);
    }

    @Test
    public void testUpdateState() {
        // Setup
        final String message = "accepted";
        final PaymentStatus status = PaymentStatus.PENDING;

        // Run the test
        paymentUnderTest.updateState(message, status);

        // Verify the results
        assertEquals("initialMessage - accepted", paymentUnderTest.getMessage());
        assertEquals(status, paymentUnderTest.getStatus());
    }
}
