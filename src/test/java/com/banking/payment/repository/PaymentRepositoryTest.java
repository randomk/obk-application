package com.banking.payment.repository;

import com.banking.payment.exception.InvalidPaymentException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class PaymentRepositoryTest {

    @Mock
    private PaymentOrderQueue mockPaymentOrderQueue;

    private PaymentRepository paymentRepositoryUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        paymentRepositoryUnderTest = new PaymentRepository(mockPaymentOrderQueue);
    }

    @Test
    public void testAcceptFundTransferPaymentOrder() {
        // Setup
        final FundTransferPaymentOrder paymentOrder = new FundTransferPaymentOrder(
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.TEN);
        final UUID expectedResult = UUID.randomUUID();

        doReturn(expectedResult).when(mockPaymentOrderQueue).putPaymentOrder(any(AbstractPaymentOrder.class));

        // Run the test
        final UUID result = paymentRepositoryUnderTest.acceptFundTransferPaymentOrder(paymentOrder);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAcceptDepositPaymentOrder() {
        // Setup
        final DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        final UUID expectedResult = UUID.randomUUID();
        doReturn(expectedResult).when(mockPaymentOrderQueue).putPaymentOrder(any(AbstractPaymentOrder.class));

        // Run the test
        final UUID result = paymentRepositoryUnderTest.acceptDepositPaymentOrder(paymentOrder);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = InvalidPaymentException.class)
    public void testProcessPaymentOrderWhenThereIsNoExistingPaymentOrder() {
        // Setup
        final AbstractPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        final String message = "message";
        final PaymentStatus status = PaymentStatus.PENDING;

        // Run the test
        paymentRepositoryUnderTest.processPaymentOrder(paymentOrder, message, status);

        // Verify the results
        final Payment payment = paymentRepositoryUnderTest.getPayment(paymentOrder.getId()).getPayment();
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

    }

    @Test
    public void testProcessPaymentOrderWhenThereIsExistingPaymentOrder() {
        // Setup
        final DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        final String message = "message";
        final PaymentStatus status = PaymentStatus.PENDING;

        doReturn(paymentOrder.getId()).when(mockPaymentOrderQueue).putPaymentOrder(any(AbstractPaymentOrder.class));

        paymentRepositoryUnderTest.acceptDepositPaymentOrder(paymentOrder);
        // Run the test
        paymentRepositoryUnderTest.processPaymentOrder(paymentOrder, message, status);

        // Verify the results
        final Payment payment = paymentRepositoryUnderTest.getPayment(paymentOrder.getId()).getPayment();
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

    }

    @Test(expected = InvalidPaymentException.class)
    public void testGetPaymentWhenNoPaymentExists() {
        // Setup
        final UUID paymentId = UUID.randomUUID();
        final Payment expectedResult = null;

        // Run the test
        final Payment result = paymentRepositoryUnderTest.getPayment(paymentId).getPayment();

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetPaymentWhenPaymentExists() {
        // Setup
        final DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        doReturn(paymentOrder.getId()).when(mockPaymentOrderQueue).putPaymentOrder(any(AbstractPaymentOrder.class));

        paymentRepositoryUnderTest.acceptDepositPaymentOrder(paymentOrder);
        // Run the test
        final Payment result = paymentRepositoryUnderTest.getPayment(paymentOrder.getId()).getPayment();

        // Verify the results
        assertEquals(PaymentStatus.PENDING, result.getStatus());
    }
}
