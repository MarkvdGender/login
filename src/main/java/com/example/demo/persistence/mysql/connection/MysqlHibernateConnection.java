package com.example.demo.persistence.mysql.connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MysqlHibernateConnection {

    private static MysqlHibernateConnection instance;
    private static SessionFactory sessionFactory;

    private MysqlHibernateConnection(){
    }

    public static SessionFactory getSessionFactory() {
        if(instance==null){
            instance = new MysqlHibernateConnection();
        }
        if(sessionFactory == null){
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        }

        return sessionFactory;
    }

}