package com.banking.account.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID accountId) {
        super("Account " + accountId.toString() + " not found");
    }
}
