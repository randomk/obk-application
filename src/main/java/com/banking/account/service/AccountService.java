package com.banking.account.service;

import com.banking.account.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.UUID;


public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public UUID createAccount() {
        return accountRepository.createAccount();
    }

    public BigDecimal getAccountBalance(UUID accountId) {
        return accountRepository.getAccountBalance(accountId);
    }

    public boolean isAccountPresent(UUID accountId) {
        return accountRepository.isAccountPresent(accountId);
    }

    public void debitToAccountBalance(UUID accountId, BigDecimal amount) {
        accountRepository.debitToAccountBalance(accountId,amount);
    }

    public void creditToAccountBalance(UUID accountId, BigDecimal amount) {
        accountRepository.creditToAccountBalance(accountId,amount);
    }
}
