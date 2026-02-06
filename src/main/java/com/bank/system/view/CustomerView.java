package com.bank.system.view;

import com.bank.system.model.BankAccount;
import com.bank.system.model.Transaction;
import java.util.List;
import java.util.Scanner;

public class CustomerView {
  private final Scanner scanner = new Scanner(System.in);

  public String showMenu(String name) {
    System.out.println("\n--- Customer Menu (Welcome " + name + ") ---");
    System.out.println("1. View Balance");
    System.out.println("2. Deposit");
    System.out.println("3. Withdraw");
    System.out.println("4. Transaction History");
    System.out.println("5. Logout");
    System.out.print("Choice: ");
    return scanner.nextLine();
  }

  public void displayBalance(double balance) {
    System.out.printf("\nCurrent Balance: $%.2f%n", balance);
  }

  public void displayHistory(List<Transaction> history) {
    System.out.println("\n--- Transaction History ---");
    System.out.printf("%-25s %-10s %-15s %-10s%n", "Timestamp", "Type", "Amount", "ID");
    for (Transaction tx : history) {
      System.out.printf("%-25s %-10s $%-14.2f %s%n",
          tx.getTimestamp(), tx.getType(), tx.getAmount(), tx.getTransactionId());
    }
  }

  public String getInput(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine();
  }
}
