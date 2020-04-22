package com.banking.payment.repository;

import java.math.BigDecimal;
import java.util.UUID;

public class AbstractPaymentOrder {
  protected final UUID id;
  protected UUID beneficiaryAccountId;
  protected BigDecimal amount;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    protected Payment payment;

    protected AbstractPaymentOrder() {
        id = UUID.randomUUID();
    }

    protected AbstractPaymentOrder(UUID beneficiaryAccountId, BigDecimal amount) {
        this.beneficiaryAccountId = beneficiaryAccountId;
        this.amount = amount;
    id = UUID.randomUUID();
  }

  public UUID getBeneficiaryAccountId() {
    return beneficiaryAccountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public UUID getId() {
    return id;
  }
}
