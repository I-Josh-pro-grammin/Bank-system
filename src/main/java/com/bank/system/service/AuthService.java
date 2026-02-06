package com.bank.system.service;

import com.bank.system.model.User;
import com.bank.system.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {

  private final UserRepository userRepository = new UserRepository();
  private User currentUser;

  public boolean signup(String username, String password, User.Role role) {
    if (userRepository.findByUsername(username).isPresent()) {
      System.out.println("Username already exists.");
      return false;
    }

    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    User user = new User(username, hashedPassword, role);
    userRepository.save(user);
    System.out.println("User registered successfully.");
    return true;
  }

  public boolean login(String username, String password) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (BCrypt.checkpw(password, user.getPassword())) {
        this.currentUser = user;
        System.out.println("Login successful!");
        return true;
      }
    }
    System.out.println("Invalid username or password.");
    return false;
  }

  public void logout() {
    this.currentUser = null;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public boolean isLoggedIn() {
    return currentUser != null;
  }
}
