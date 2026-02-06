package com.bank.system.repository;

import com.bank.system.model.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerRepository {

  public void save(Customer customer) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(customer);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
    }
  }
}
