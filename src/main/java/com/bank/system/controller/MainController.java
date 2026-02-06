package com.bank.system.controller;

import com.bank.system.exception.InsufficientFundsException;
import com.bank.system.model.BankAccount;
import com.bank.system.model.Transaction;
import com.bank.system.model.User;
import com.bank.system.service.AuthService;
import com.bank.system.service.BankService;
import com.bank.system.view.AdminView;
import com.bank.system.view.AuthView;
import com.bank.system.view.CustomerView;

import java.util.UUID;

public class MainController {
  private final AuthService authService = new AuthService();
  private final BankService bankService = new BankService();
  private final AuthView authView = new AuthView();
  private final AdminView adminView = new AdminView();
  private final CustomerView customerView = new CustomerView();

  public void start() {
    authView.showWelcomeMessage();
    boolean running = true;
    while (running) {
      String choice = authView.showAuthMenu();
      switch (choice) {
        case "1":
          handleLogin();
          break;
        case "2":
          handleSignup();
          break;
        case "3":
          running = false;
          System.out.println("Goodbye!");
          break;
        default:
          System.out.println("Invalid choice.");
      }
    }
  }

  private void handleLogin() {
    String username = authView.getUsername();
    String password = authView.getPassword();
    if (authService.login(username, password)) {
      User user = authService.getCurrentUser();
      if (user.getRole() == User.Role.ADMIN) {
        runAdminSession();
      } else {
        runCustomerSession();
      }
    }
  }

  private void handleSignup() {
    String username = authView.getUsername();
    String password = authView.getPassword();
    String roleStr = authView.getRole();
    try {
      User.Role role = User.Role.valueOf(roleStr);
      authService.signup(username, password, role);
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid role. Use ADMIN or CUSTOMER.");
    }
  }

  private void runAdminSession() {
    boolean loggedIn = true;
    while (loggedIn) {
      String choice = adminView.showMenu();
      switch (choice) {
        case "1":
          String accId = adminView.getInput("Enter Account ID: ");
          String name = adminView.getInput("Enter Customer Name: ");
          String email = adminView.getInput("Enter Customer Email: ");
          String userTarget = adminView.getInput("Enter Customer Username to link: ");
          // Simple logic: admin links account to an existing user
          authService.login(userTarget, ""); // This is just a placeholder, real logic needs user search
          // Let's simplify and just create a new customer linked to a temporary user or
          // just create user too
          System.out.println("Account creation simplified for this demo. Admin needs to create User first.");
          break;
        case "2":
          adminView.displayAccounts(bankService.getAllAccounts());
          break;
        case "3":
          authService.logout();
          loggedIn = false;
          break;
      }
    }
  }

  private void runCustomerSession() {
    User user = authService.getCurrentUser();
    // For simplicity, we assume account ID matches username or is linked
    // In a real app, we'd search for the account linked to this user
    String accountId = user.getUsername(); // Hack for this demo
    BankAccount account = bankService.getAccount(accountId);

    if (account == null) {
      System.out.println("No bank account found for your user. Please contact an admin.");
      authService.logout();
      return;
    }

    boolean loggedIn = true;
    while (loggedIn) {
      String choice = customerView.showMenu(account.getCustomer().getName());
      switch (choice) {
        case "1":
          customerView.displayBalance(account.getBalance());
          break;
        case "2":
          double depAmt = Double.parseDouble(customerView.getInput("Amount to deposit: "));
          Transaction depTx = new Transaction(UUID.randomUUID().toString(), account, Transaction.Type.DEPOSIT, depAmt);
          try {
            bankService.processTransaction(depTx);
          } catch (InsufficientFundsException e) {
            e.printStackTrace();
          }
          break;
        case "3":
          double withAmt = Double.parseDouble(customerView.getInput("Amount to withdraw: "));
          Transaction withTx = new Transaction(UUID.randomUUID().toString(), account, Transaction.Type.WITHDRAWAL,
              withAmt);
          try {
            bankService.processTransaction(withTx);
          } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
          }
          break;
        case "4":
          customerView.displayHistory(account.getTransactionHistory());
          break;
        case "5":
          authService.logout();
          loggedIn = false;
          break;
      }
    }
  }
}
