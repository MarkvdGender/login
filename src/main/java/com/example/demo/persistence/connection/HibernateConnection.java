package com.example.demo.persistence.connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnection {

    private static HibernateConnection instance;
    private static SessionFactory sessionFactory;

    private HibernateConnection(){
    }

    public static SessionFactory getSessionFactory() {
        if(instance==null){
            instance = new HibernateConnection();
        }
        if(sessionFactory == null){
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }

        return sessionFactory;
    }

}