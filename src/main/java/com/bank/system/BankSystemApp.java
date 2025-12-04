package com.bank.system;

import com.bank.system.model.BankAccount;
import com.bank.system.model.Transaction;
import com.bank.system.processor.TransactionProcessor;
import com.bank.system.service.BankService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BankSystemApp {

  private static final Scanner scanner = new Scanner(System.in);
  private static final BankService bankService = new BankService();

  public static void main(String[] args) {
    System.out.println("=== Bank Transaction Processing System ===");

    boolean running = true;
    while (running) {
      System.out.println("\n--- Main Menu ---");
      System.out.println("1. Bank Admin");
      System.out.println("2. Bank Customer");
      System.out.println("3. Exit");
      System.out.print("Enter your choice: ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          runAdminMenu();
          break;
        case "2":
          runCustomerMenu();
          break;
        case "3":
          running = false;
          System.out.println("Exiting...");
          break;
        default:
          System.out.println("Invalid option. Please try again.");
      }
    }
    scanner.close();
  }

  // --- Admin Section ---

  private static void runAdminMenu() {
    boolean back = false;
    while (!back) {
      System.out.println("\n--- Admin Menu ---");
      System.out.println("1. Create New Account");
      System.out.println("2. Process Transaction File");
      System.out.println("3. View All Accounts");
      System.out.println("4. Back to Main Menu");
      System.out.print("Enter your choice: ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          createAccount();
          break;
        case "2":
          processTransactionFile();
          break;
        case "3":
          printFinalReport();
          break;
        case "4":
          back = true;
          break;
        default:
          System.out.println("Invalid option.");
      }
    }
  }

  private static void createAccount() {
    System.out.println("\n--- Create Account ---");
    System.out.print("Enter Account ID: ");
    String accountId = scanner.nextLine();

    System.out.print("Enter Customer ID: ");
    String customerId = scanner.nextLine();

    System.out.print("Enter Customer Name: ");
    String name = scanner.nextLine();

    System.out.print("Enter Customer Email: ");
    String email = scanner.nextLine();

    double balance = 0;
    boolean validBalance = false;
    while (!validBalance) {
      System.out.print("Enter Initial Balance: ");
      try {
        balance = Double.parseDouble(scanner.nextLine());
        validBalance = true;
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format.");
      }
    }

    bankService.createAccount(accountId, customerId, name, email, balance);
  }

  private static void processTransactionFile() {
    System.out.println("\n--- Process Transaction File ---");
    System.out.print("Enter full path to transaction file (CSV): ");
    String filePath = scanner.nextLine();

    File file = new File(filePath);
    if (!file.exists()) {
      System.out.println("Error: File not found at " + filePath);
      return;
    }

    TransactionProcessor processor = new TransactionProcessor(file, bankService);
    Thread thread = new Thread(processor);
    thread.start();

    try {
      thread.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.out.println("Processing interrupted.");
    }
  }

  private static void printFinalReport() {
    System.out.println("\n=== Account Report ===");
    System.out.printf("%-10s %-20s %-15s%n", "Account ID", "Customer Name", "Balance");
    System.out.println("-----------------------------------------------");

    Map<String, BankAccount> accounts = bankService.getAllAccounts();
    if (accounts.isEmpty()) {
      System.out.println("No accounts found.");
    } else {
      for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
        BankAccount acc = entry.getValue();
        System.out.printf("%-10s %-20s $%.2f%n",
            acc.getAccountId(),
            acc.getCustomer().getName(),
            acc.getBalance());
      }
    }
    System.out.println("-----------------------------------------------");
  }

  // --- Customer Section ---

  private static void runCustomerMenu() {
    System.out.println("\n--- Customer Login ---");
    System.out.print("Enter your Account ID: ");
    String accountId = scanner.nextLine();

    BankAccount account = bankService.getAccount(accountId);
    if (account == null) {
      System.out.println("Account not found.");
      return;
    }

    System.out.println("Welcome, " + account.getCustomer().getName() + "!");

    boolean back = false;
    while (!back) {
      System.out.println("\n--- Customer Menu ---");
      System.out.println("1. View Balance");
      System.out.println("2. View Transaction History");
      System.out.println("3. Logout");
      System.out.print("Enter your choice: ");

      String choice = scanner.nextLine();

      switch (choice) {
        case "1":
          System.out.printf("Current Balance: $%.2f%n", account.getBalance());
          break;
        case "2":
          printTransactionHistory(account);
          break;
        case "3":
          back = true;
          break;
        default:
          System.out.println("Invalid option.");
      }
    }
  }

  private static void printTransactionHistory(BankAccount account) {
    System.out.println("\n--- Transaction History ---");
    List<Transaction> history = account.getTransactionHistory();
    if (history.isEmpty()) {
      System.out.println("No transactions found.");
    } else {
      System.out.printf("%-10s %-10s %-15s %-10s%n", "Tx ID", "Type", "Amount", "Time");
      System.out.println("-----------------------------------------------------");
      for (Transaction tx : history) {
        System.out.printf("%-10s %-10s $%-14.2f %s%n",
            tx.getTransactionId(),
            tx.getType(),
            tx.getAmount(),
            tx.getTimestamp().toString());
      }
    }
  }
}
