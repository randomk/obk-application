package com.banking.payment.exception;

public class UnableToProcessPaymentOrderException extends RuntimeException {

    public UnableToProcessPaymentOrderException() {
        super("Unable to accept Payment Orders at the moment");
    }
}
