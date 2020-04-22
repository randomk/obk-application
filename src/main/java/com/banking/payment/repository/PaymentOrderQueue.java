package com.banking.payment.repository;

import com.banking.payment.exception.UnableToProcessPaymentOrderException;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class PaymentOrderQueue {

    private final BlockingQueue<AbstractPaymentOrder> paymentOrderBlockingQueue;

    public PaymentOrderQueue(BlockingQueue<AbstractPaymentOrder> paymentOrderBlockingQueue) {
        this.paymentOrderBlockingQueue = paymentOrderBlockingQueue;
    }

    public AbstractPaymentOrder getPaymentOrderFromQueue() {
        AbstractPaymentOrder paymentOrder;
        try {
            paymentOrder = paymentOrderBlockingQueue.take();
        } catch (InterruptedException ex) {
            throw new UnableToProcessPaymentOrderException();
        }
        return paymentOrder;
    }

    public UUID putPaymentOrder(AbstractPaymentOrder paymentOrder) {
        try {
            paymentOrderBlockingQueue.put(paymentOrder);
        } catch (InterruptedException ex) {
            throw new UnableToProcessPaymentOrderException();
        }
        return paymentOrder.getId();
    }
}
