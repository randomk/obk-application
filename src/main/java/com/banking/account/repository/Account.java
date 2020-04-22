package com.banking.account.repository;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private final UUID id;

    private BigDecimal balance;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }

    Account() {
        this.id = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void credit(BigDecimal amount){
        balance=balance.add(amount);
    }

    public void debit(BigDecimal amount){
        balance=balance.subtract(amount);
    }
}
