package com.bank.system.repository;

import com.bank.system.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository {

  public void save(User user) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(user);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null)
        transaction.rollback();
      e.printStackTrace();
    }
  }

  public Optional<User> findByUsername(String username) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
      query.setParameter("username", username);
      return query.uniqueResultOptional();
    }
  }
}
