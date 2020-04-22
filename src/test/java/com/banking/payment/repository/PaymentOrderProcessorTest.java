package com.banking.payment.repository;

import com.banking.payment.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PaymentOrderProcessorTest {

    @Mock
    private PaymentService mockPaymentService;

    @Mock
    private PaymentOrderQueue mockPaymentOrderQueue;

    private PaymentOrderProcessor paymentOrderProcessorUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void ableToExtractPaymentTransferOrderFromQueueAndProcess() {
        DepositPaymentOrder paymentOrder = new DepositPaymentOrder(UUID.randomUUID(), new BigDecimal("12"));
        // Setup
        doReturn(paymentOrder).when(mockPaymentOrderQueue).getPaymentOrderFromQueue();
        doNothing().when(mockPaymentService).processPaymentOrder(paymentOrder);
        ArgumentCaptor<AbstractPaymentOrder> paymentOrderArgumentCaptor = ArgumentCaptor
                .forClass(AbstractPaymentOrder.class);
        // Run the test
        paymentOrderProcessorUnderTest = new PaymentOrderProcessor(mockPaymentService, mockPaymentOrderQueue);
        paymentOrderProcessorUnderTest.extractAndProcessPaymentTransferOrder();
        verify(mockPaymentService, atLeast(1)).processPaymentOrder(paymentOrderArgumentCaptor.capture());

        // Verify the results
        assertEquals(paymentOrder, paymentOrderArgumentCaptor.getAllValues().get(0));
    }
}
