package com.banking.payment.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class PaymentOrderQueueTest {

    @Mock
    private BlockingQueue<AbstractPaymentOrder> mockPaymentOrderBlockingQueue;

    private PaymentOrderQueue paymentOrderQueueUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        paymentOrderQueueUnderTest = new PaymentOrderQueue(mockPaymentOrderBlockingQueue);
    }

    @Test
    public void testGetPaymentOrderFromQueue() {
        // Setup
        final AbstractPaymentOrder expectedResult = null;

        // Run the test
        final AbstractPaymentOrder result = paymentOrderQueueUnderTest.getPaymentOrderFromQueue();

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPutPaymentOrder() {
        // Setup
        final AbstractPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), new BigDecimal(50));
        final UUID expectedResult = UUID.randomUUID();

        // Run the test
        final UUID result = paymentOrderQueueUnderTest.putPaymentOrder(paymentOrder);

        // Verify the results
        assertEquals(paymentOrder.getId(), result);
    }
}
