package com.bank.system.model;

import com.bank.system.exception.InsufficientFundsException;

public class BankAccount {
    private String accountId;
    private Customer customer;
    private double balance;

    private java.util.List<Transaction> transactionHistory = new java.util.ArrayList<>();

    public BankAccount(String accountId, Customer customer, double initialBalance) {
        this.accountId = accountId;
        this.customer = customer;
        this.balance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Synchronized for thread safety
    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void addTransaction(Transaction tx) {
        transactionHistory.add(tx);
    }

    public synchronized java.util.List<Transaction> getTransactionHistory() {
        return new java.util.ArrayList<>(transactionHistory);
    }

    // Synchronized for thread safety
    public synchronized void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited: " + amount + " to " + accountId);
        }
    }

    // Synchronized for thread safety
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds for account " + accountId + ". Balance: " + balance + ", Requested: " + amount);
        }
        this.balance -= amount;
        System.out.println(Thread.currentThread().getName() + " withdrew: " + amount + " from " + accountId);
    }

    @Override
    public String toString() {
        return String.format("Account[ID=%s, Holder=%s, Balance=%.2f]", accountId, customer.getName(), balance);
    }
}
