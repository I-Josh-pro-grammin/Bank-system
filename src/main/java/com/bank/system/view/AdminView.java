package com.bank.system.view;

import com.bank.system.model.BankAccount;
import java.util.List;
import java.util.Scanner;

public class AdminView {
  private final Scanner scanner = new Scanner(System.in);

  public String showMenu() {
    System.out.println("\n--- Admin Menu ---");
    System.out.println("1. Create New Account");
    System.out.println("2. View All Accounts");
    System.out.println("3. Logout");
    System.out.print("Choice: ");
    return scanner.nextLine();
  }

  public void displayAccounts(List<BankAccount> accounts) {
    System.out.println("\n=== All Accounts ===");
    System.out.printf("%-15s %-20s %-15s%n", "Account ID", "Customer Name", "Balance");
    for (BankAccount acc : accounts) {
      System.out.printf("%-15s %-20s $%.2f%n",
          acc.getAccountId(),
          acc.getCustomer().getName(),
          acc.getBalance());
    }
  }

  public String getInput(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine();
  }
}
