package com.bank.system.model;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type {
        DEPOSIT, WITHDRAWAL
    }

    private String transactionId;
    private String accountId;
    private Type type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, String accountId, Type type, double amount) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Tx[ID=%s, Acct=%s, Type=%s, Amt=%.2f]", transactionId, accountId, type, amount);
    }
}
