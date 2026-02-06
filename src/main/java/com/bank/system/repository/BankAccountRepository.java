package com.bank.system.repository;

import com.bank.system.model.BankAccount;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class BankAccountRepository {

  public void save(BankAccount account) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.merge(account);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
    }
  }

  public Optional<BankAccount> findById(String accountId) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return Optional.ofNullable(session.get(BankAccount.class, accountId));
    }
  }

  public List<BankAccount> findAll() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery("FROM BankAccount", BankAccount.class).list();
    }
  }
}
