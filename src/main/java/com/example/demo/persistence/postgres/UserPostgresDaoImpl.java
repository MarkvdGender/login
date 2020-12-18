package com.example.demo.persistence.postgres;

import com.example.demo.domain.User;
import com.example.demo.persistence.UserDao;
import com.example.demo.persistence.connection.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserPostgresDaoImpl implements UserDao{

    private static UserDao instance;
    private static SessionFactory sessionFactory;
    private Session session;

    private UserPostgresDaoImpl(){
        sessionFactory = HibernateConnection.getSessionFactory();

    }

    public static UserDao getInstance(){
        if(instance==null){
            instance = new UserPostgresDaoImpl();
        }
        return instance;
    }

    @Override
    public void save(User u) {

        session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();

        session.save(u);
        tr.commit();
        session.close();

    }

    @Override
    public byte[] findSalt(String username) {
        session = sessionFactory.openSession();
        Query query = session.createQuery("Select a from User a WHERE username = :username", User.class);
        query.setParameter("username", username);
        User u = (User) query.uniqueResult();
        session.close();
        return u.getSalt();
    }

    @Override
    public boolean login(User u) {
        session = sessionFactory.openSession();
        Query query = session.createQuery("Select a from User a WHERE username = :username AND password = :password", User.class);
        query.setParameter("username", u.getUsername());
        query.setParameter("password", u.getPassword());
        if(query.uniqueResult()!=null){
            return true;
        }
        session.close();
        return false;
    }

    @Override
    public List<User> leak() {
        session = sessionFactory.openSession();
        List<User> users = session.createQuery("Select a from User a ", User.class).getResultList();
        session.close();
        return users;
    }
}
