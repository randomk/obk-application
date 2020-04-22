package com.banking.payment.repository;


import com.banking.payment.service.PaymentService;

import java.util.Objects;

public class PaymentOrderProcessor extends Thread {

    private final PaymentService paymentService;

    private final PaymentOrderQueue paymentOrderQueue;

    public PaymentOrderProcessor(PaymentService paymentService, PaymentOrderQueue paymentOrderQueue) {
        this.paymentService = paymentService;
        this.paymentOrderQueue = paymentOrderQueue;
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            extractAndProcessPaymentTransferOrder();
        }
    }

    void extractAndProcessPaymentTransferOrder() {
        AbstractPaymentOrder paymentOrder = null;
        paymentOrder = paymentOrderQueue.getPaymentOrderFromQueue();
        if (Objects.nonNull(paymentOrder)) {
            paymentService.processPaymentOrder(paymentOrder);
        }
    }
}