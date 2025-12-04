package com.bank.system.service;

import com.bank.system.exception.InsufficientFundsException;
import com.bank.system.model.BankAccount;
import com.bank.system.model.Customer;
import com.bank.system.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    // ConcurrentHashMap for thread-safe access to accounts
    private Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
    private FileStorageService storageService = new FileStorageService();

    public BankService() {
        // Load data on startup
        this.accounts = storageService.loadAccounts();
        List<Transaction> transactions = storageService.loadTransactions();

        // Replay transactions to history (balances are already loaded from accounts
        // file)
        for (Transaction tx : transactions) {
            BankAccount account = accounts.get(tx.getAccountId());
            if (account != null) {
                account.addTransaction(tx);
            }
        }
    }

    public void createAccount(String accountId, String customerId, String name, String email, double initialBalance) {
        Customer customer = new Customer(customerId, name, email);
        BankAccount account = new BankAccount(accountId, customer, initialBalance);
        accounts.put(accountId, account);
        storageService.saveAccounts(accounts); // Save state
        System.out.println("Created account: " + account);
    }

    public BankAccount getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public void processTransaction(Transaction transaction) throws InsufficientFundsException {
        BankAccount account = accounts.get(transaction.getAccountId());
        if (account == null) {
            System.err.println("Account not found for transaction: " + transaction.getTransactionId());
            return;
        }

        switch (transaction.getType()) {
            case DEPOSIT:
                account.deposit(transaction.getAmount());
                break;
            case WITHDRAWAL:
                account.withdraw(transaction.getAmount());
                break;
        }

        account.addTransaction(transaction);
        storageService.saveTransaction(transaction); // Append transaction
        storageService.saveAccounts(accounts); // Update balances
    }

    public Map<String, BankAccount> getAllAccounts() {
        return accounts;
    }
}
