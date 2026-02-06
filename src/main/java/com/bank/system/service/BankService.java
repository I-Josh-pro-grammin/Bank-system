package com.bank.system.service;

import com.bank.system.exception.InsufficientFundsException;
import com.bank.system.model.BankAccount;
import com.bank.system.model.Customer;
import com.bank.system.model.Transaction;
import com.bank.system.model.User;
import com.bank.system.repository.BankAccountRepository;
import com.bank.system.repository.CustomerRepository;
import com.bank.system.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

public class BankService {
    private final BankAccountRepository accountRepository = new BankAccountRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public void createAccount(String accountId, User user, String name, String email, double initialBalance) {
        Customer customer = new Customer(accountId, name, email);
        customer.setUser(user);
        customerRepository.save(customer);

        BankAccount account = new BankAccount(accountId, customer, initialBalance);
        accountRepository.save(account);
        System.out.println("Created account: " + account);
    }

    public BankAccount getAccount(String accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public void processTransaction(Transaction transaction) throws InsufficientFundsException {
        BankAccount account = transaction.getAccount();
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
        accountRepository.save(account); // This will save account and transactions due to cascade
    }

    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }
}
