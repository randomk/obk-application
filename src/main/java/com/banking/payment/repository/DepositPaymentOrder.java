package com.banking.payment.repository;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositPaymentOrder extends AbstractPaymentOrder {

    DepositPaymentOrder() {
        super();
    }

    public DepositPaymentOrder(UUID beneficiaryAccountId, BigDecimal amount) {
        super(beneficiaryAccountId, amount);
        this.beneficiaryAccountId = beneficiaryAccountId;
        this.amount = amount;
    }
}
