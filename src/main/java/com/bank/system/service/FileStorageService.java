package com.bank.system.service;

import com.bank.system.model.BankAccount;
import com.bank.system.model.Customer;
import com.bank.system.model.Transaction;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileStorageService {
  private static final String DATA_DIR = "data";
  private static final String ACCOUNTS_FILE = DATA_DIR + "/accounts.csv";
  private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.csv";

  public FileStorageService() {
    File dir = new File(DATA_DIR);
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  public void saveAccounts(Map<String, BankAccount> accounts) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
      for (BankAccount acc : accounts.values()) {
        // AccountId,CustomerId,Name,Email,Balance
        writer.printf("%s,%s,%s,%s,%.2f%n",
            acc.getAccountId(),
            acc.getCustomer().getCustomerId(),
            acc.getCustomer().getName(),
            acc.getCustomer().getEmail(),
            acc.getBalance());
      }
    } catch (IOException e) {
      System.err.println("Error saving accounts: " + e.getMessage());
    }
  }

  public Map<String, BankAccount> loadAccounts() {
    Map<String, BankAccount> accounts = new HashMap<>();
    File file = new File(ACCOUNTS_FILE);
    if (!file.exists())
      return accounts;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length == 5) {
          String accId = parts[0];
          String custId = parts[1];
          String name = parts[2];
          String email = parts[3];
          double balance = Double.parseDouble(parts[4]);

          Customer customer = new Customer(custId, name, email);
          BankAccount account = new BankAccount(accId, customer, balance);
          accounts.put(accId, account);
        }
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error loading accounts: " + e.getMessage());
    }
    return accounts;
  }

  public void saveTransaction(Transaction tx) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
      // TxId,AccountId,Type,Amount,Timestamp
      writer.printf("%s,%s,%s,%.2f,%s%n",
          tx.getTransactionId(),
          tx.getAccountId(),
          tx.getType(),
          tx.getAmount(),
          tx.getTimestamp());
    } catch (IOException e) {
      System.err.println("Error saving transaction: " + e.getMessage());
    }
  }

  public List<Transaction> loadTransactions() {
    List<Transaction> transactions = new ArrayList<>();
    File file = new File(TRANSACTIONS_FILE);
    if (!file.exists())
      return transactions;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
          String txId = parts[0];
          String accId = parts[1];
          Transaction.Type type = Transaction.Type.valueOf(parts[2]);
          double amount = Double.parseDouble(parts[3]);

          transactions.add(new Transaction(txId, accId, type, amount));
        }
      }
    } catch (IOException | IllegalArgumentException e) {
      System.err.println("Error loading transactions: " + e.getMessage());
    }
    return transactions;
  }
}
