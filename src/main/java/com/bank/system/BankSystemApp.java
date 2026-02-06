package com.bank.system;

import com.bank.system.controller.MainController;
import com.bank.system.repository.HibernateUtil;

public class BankSystemApp {

  public static void main(String[] args) {
    try {
      MainController controller = new MainController();
      controller.start();
    } finally {
      HibernateUtil.shutdown();
    }
  }
}
