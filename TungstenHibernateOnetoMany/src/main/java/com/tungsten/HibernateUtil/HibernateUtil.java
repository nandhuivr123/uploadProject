package com.tungsten.HibernateUtil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static final ThreadLocal<Session> threadLocal;
	
	static {
	    try {
	    	System.out.println("build session factory");
	    	Configuration configObj = new Configuration();
			configObj.configure("hibernate.cfg.xml");
	        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();
	        sessionFactory = configObj.buildSessionFactory(serviceRegistry);
	        threadLocal = new ThreadLocal<Session>();

	    } catch(Throwable t){
	        t.printStackTrace();
	        throw new ExceptionInInitializerError(t);
	    }
	}
	public static Session openCurrentSession() throws HibernateException {
		System.out.println("open session..");
	    Session session = threadLocal.get();
	    if(session == null){
	        session = sessionFactory.openSession();
	        threadLocal.set(session);
	    }
	    return session;
	}

	
	public static void closeSession() {
		try {
	    Session session = threadLocal.get();
	    if(session != null){
	        session.close();
	        threadLocal.set(null);
	    }
		}catch(Throwable t){
	        t.printStackTrace();
	        throw new ExceptionInInitializerError(t);
	    }
	}

	public static void closeSessionFactory() {
		try {
	    sessionFactory.close();
	    StandardServiceRegistryBuilder.destroy(serviceRegistry);
	  }catch(Throwable t){
	        t.printStackTrace();
	        throw new ExceptionInInitializerError(t);
	    }
	
	}
}