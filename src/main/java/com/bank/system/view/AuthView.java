package com.bank.system.view;

import java.util.Scanner;

public class AuthView {
  private final Scanner scanner = new Scanner(System.in);

  public void showWelcomeMessage() {
    System.out.println("\n=== Welcome to the Antigravity Bank ===");
  }

  public String getUsername() {
    System.out.print("Enter username: ");
    return scanner.nextLine();
  }

  public String getPassword() {
    System.out.print("Enter password: ");
    return scanner.nextLine();
  }

  public String getRole() {
    System.out.print("Enter role (ADMIN/CUSTOMER): ");
    return scanner.nextLine().toUpperCase();
  }

  public String showAuthMenu() {
    System.out.println("\n--- Authentication ---");
    System.out.println("1. Login");
    System.out.println("2. Signup");
    System.out.println("3. Exit");
    System.out.print("Choice: ");
    return scanner.nextLine();
  }
}
