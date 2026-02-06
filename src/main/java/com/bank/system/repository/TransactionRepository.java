package com.bank.system.repository;

import com.bank.system.model.Transaction;
import org.hibernate.Session;

public class TransactionRepository {

  public void save(Transaction tx) {
    org.hibernate.Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(tx);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
    }
  }
}
