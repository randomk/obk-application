package com.banking.payment.repository;

import java.math.BigDecimal;
import java.util.UUID;

public class FundTransferPaymentOrder extends AbstractPaymentOrder {

    private UUID payeeAccountId;

    @Override
    public String toString() {
        return "FundTransferPaymentOrder{" +
                "id=" + id +
                ", payeeAccountId=" + payeeAccountId +
                ", beneficiaryAccountId=" + beneficiaryAccountId +
                ", amount=" + amount +
                '}';
    }

    FundTransferPaymentOrder() {
        super();
    }

    public FundTransferPaymentOrder(UUID payeeAccountId, UUID beneficiaryAccountId, BigDecimal amount) {
        super(beneficiaryAccountId, amount);
        this.payeeAccountId = payeeAccountId;
    }

    public UUID getPayeeAccountId() {
        return payeeAccountId;
    }

}
