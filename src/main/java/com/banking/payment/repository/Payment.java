package com.banking.payment.repository;

public class Payment {

    private String message = "";

    private PaymentStatus status;

    Payment(String message, PaymentStatus status) {
        this.message = message;
        this.status = status;
    }

    public void updateState(String message, PaymentStatus status) {
        this.message += (" - " + message);
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
