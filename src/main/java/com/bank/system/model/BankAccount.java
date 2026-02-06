package com.bank.system.model;

import com.bank.system.exception.InsufficientFundsException;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @Column(name = "account_id")
    private String accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionHistory = new ArrayList<>();

    public BankAccount() {
    }

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

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void addTransaction(Transaction tx) {
        transactionHistory.add(tx);
        tx.setAccount(this);
    }

    public synchronized List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public synchronized void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Insufficient funds for account " + accountId + ". Balance: " + balance + ", Requested: " + amount);
        }
        this.balance -= amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Account[ID=%s, Holder=%s, Balance=%.2f]", accountId, customer.getName(), balance);
    }
}
