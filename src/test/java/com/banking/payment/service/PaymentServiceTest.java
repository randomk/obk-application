package com.banking.payment.service;

import com.banking.account.service.AccountService;
import com.banking.payment.repository.AbstractPaymentOrder;
import com.banking.payment.repository.DepositPaymentOrder;
import com.banking.payment.repository.FundTransferPaymentOrder;
import com.banking.payment.repository.PaymentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository mockPaymentRepository;

    @Mock
    private AccountService mockAccountService;

    private PaymentService paymentServiceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        paymentServiceUnderTest = new PaymentService(mockPaymentRepository, mockAccountService);
    }

    @Test
    public void testAcceptFundTransferPaymentOrder() {
        // Setup
        final FundTransferPaymentOrder paymentOrder = new FundTransferPaymentOrder(
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.TEN);
        doReturn(paymentOrder.getId()).when(mockPaymentRepository)
                .acceptFundTransferPaymentOrder(any(FundTransferPaymentOrder.class));

        // Run the test
        final UUID result = paymentServiceUnderTest.acceptFundTransferPaymentOrder(paymentOrder);

        // Verify the results
        assertEquals(paymentOrder.getId(), result);
    }

    @Test
    public void testAcceptDepositPaymentOrder() {
        // Setup
        final DepositPaymentOrder depositPaymentOrder = null;
        final UUID expectedResult = UUID.randomUUID();

        doReturn(expectedResult).when(mockPaymentRepository).acceptDepositPaymentOrder(any(DepositPaymentOrder.class));
        // Run the test
        final UUID result = paymentServiceUnderTest.acceptDepositPaymentOrder(depositPaymentOrder);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testProcessFundTransferPaymentOrder() {
        // Setup
        final FundTransferPaymentOrder paymentOrder = new FundTransferPaymentOrder(
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.TEN);
        doReturn(false).when(mockAccountService).isAccountPresent(any(UUID.class));
        doReturn(paymentOrder).when(mockPaymentRepository).getPayment(any(UUID.class));
        // Run the test
        paymentServiceUnderTest.processPaymentOrder(paymentOrder);
        final AbstractPaymentOrder payment = paymentServiceUnderTest.getPayment(paymentOrder.getId());
        // Verify the results
        assertEquals(paymentOrder.getId(), payment.getId());
    }

    @Test
    public void testProcessDepositPaymentOrder() {
        // Setup
        final DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        doReturn(false).when(mockAccountService).isAccountPresent(any(UUID.class));
        doReturn(paymentOrder).when(mockPaymentRepository).getPayment(any(UUID.class));
        // Run the test
        paymentServiceUnderTest.processPaymentOrder(paymentOrder);
        final AbstractPaymentOrder payment = paymentServiceUnderTest.getPayment(paymentOrder.getId());
        // Verify the results
        assertEquals(paymentOrder.getId(), payment.getId());
    }

    @Test
    public void testGetPayment() {
        // Setup
        final DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), BigDecimal.TEN);
        doReturn(paymentOrder).when(mockPaymentRepository).getPayment(any(UUID.class));

        // Run the test
        final AbstractPaymentOrder result = paymentServiceUnderTest.getPayment(paymentOrder.getId());

        // Verify the results
        assertEquals(paymentOrder, result);
    }
}
