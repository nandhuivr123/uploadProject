package com.tungsten.all;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.tungsten.HibernateUtil.HibernateUtil;
import com.tungsten.hibernaate.dao.Tungsten_DAO;

public class App {
	private static Logger logger=  LogManager.getLogger(App.class);
	static Session sessionObj;
	static SessionFactory sessionFactoryObj;

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	private static final ThreadLocal<Session> threadLocal;

	static {
		try {
			logger.info("build session factory");
			System.out.println("build session factory");
			Configuration configObj = new Configuration();
			configObj.configure("hibernate.cfg.xml");
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build();
			sessionFactory = configObj.buildSessionFactory(serviceRegistry);
			threadLocal = new ThreadLocal<Session>();

		} catch (Throwable t) {
			t.printStackTrace();
			throw new ExceptionInInitializerError(t);
		}
	}

	public static Session getSession() {
		System.out.println("open session..");
		Session session = threadLocal.get();
		if (session == null) {
			session = sessionFactory.openSession();
			threadLocal.set(session);
		}
		return session;
	}

	public static void closeSession() {
		Session session = threadLocal.get();
		if (session != null) {
			session.close();
			threadLocal.set(null);
		}
	}

	public static void closeSessionFactory() {
		sessionFactory.close();
		StandardServiceRegistryBuilder.destroy(serviceRegistry);
	}

	/*
	 * private static SessionFactory buildSessionFactory() {
	 * 
	 * Configuration configObj = new Configuration();
	 * configObj.configure("hibernate.cfg.xml");
	 * 
	 * 
	 * ServiceRegistry serviceRegistryObj = new
	 * StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).
	 * build(); System.out.println("sesion createdf*******");
	 * 
	 * sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj); return
	 * sessionFactoryObj; }
	 */
	public static void main(String[] args) {
		System.out.println(".......Hibernate One To Many Mapping Example.......\n");
		logger.info(">>>>>Hibernate Connection pooling<<<<<<<<<<<");
		// App app= new App();

		Tungsten_DAO app = new Tungsten_DAO();
 

		//To add new customer
		Customer cust= app.addingCustomer("Vinoth","757575","vinoth@gmail.com");
		System.out.println("Customer is entered and details updated..");
		logger.info("Customer is entered and details updated..");
	    app.updateNewNumberForCallback(cust, "999999");
		

		//placing the callback request by customer
		@SuppressWarnings("deprecation")
		Timestamp timestamp1 = new Timestamp(121, 6, 18, 22, 05, 30, 0);
		@SuppressWarnings("deprecation")
		Timestamp timestamp2 = new Timestamp(122, 7, 18, 15, 05, 30, 0);
		
		//current timestamp
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		//currentTimeStamp using Data object
		Date date = new Date();
		Timestamp currentTimeStampUsingDateObj = new Timestamp(date.getTime());
		
		CallbackRequests callbackRequests= app.placeCallBackRequest(timestamp1,currentTimeStampUsingDateObj, "pending", cust);

		if(callbackRequests==null) { 
			System.out.println("previous call is pending.. not taking up the request disconnect..." ); 
			logger.info("previous call is pending.. not taking up the request disconnect...");
		}

		else {
			// using this we can get callbackrequest object by passing request id
			//Long reqid = Long.parseLong("3");
			//callbackRequests = app.getCallBackRequestObjectByRequestID(reqid);

			//To add new agent into agent table
			Agent agent = app.addingAgent("Nirmala", "******", "nirmala@gmail.com");
			app.updatingAgentIDInCallBackRequest(agent, callbackRequests);

			//To update status by agent 
			logger.info("after the conversation with customer agent updating the status..");
			System.out.println("after the conversation with customer agent updating the status..");
			app.updateTheStatusByAgent(callbackRequests, "completed");

			//updating the log
			CallbackLogs logs = app.updatingTheLogs("15:51", "Great call", callbackRequests);
		}
		//we need close session factory when application goes to end
		HibernateUtil.closeSessionFactory();

		System.out.println("\n.......Records Saved Successfully To The Database.......");
		logger.info("\\n.......Records Saved Successfully To The Database.......");
	}

	public Customer addingCustomer(String name, String phone_no, String mail) {
		System.out.println(".......Adding the customer.......\n");

		Customer cus_obj = null;
		Session session;
		try {
			session = getSession();
			session.beginTransaction();
			cus_obj = new Customer(name, phone_no, mail);
			session.save(cus_obj);
			session.getTransaction().commit();
			return cus_obj;

		} catch (Exception sqlException) {

			System.out.println("\n.......Transaction Is Being Rolled Back while addingCustomer.......");
			// sessionObj.getTransaction().rollback();

			sqlException.printStackTrace();
		} finally {
			session = null;
			closeSession();

		}
		return cus_obj;
	}

	public Agent addingAgent(String agentName, String agentPhoneno, String agentMailId) {
		System.out.println(".......Adding the agents.......\n");
		Agent agent_obj = null;
		try {
			sessionObj = getSession();
			sessionObj.beginTransaction();

			agent_obj = new Agent(agentName, agentPhoneno, agentMailId);
			sessionObj.save(agent_obj);
			sessionObj.getTransaction().commit();
		} catch (Exception sqlException) {

			System.out.println("\n.......Transaction Is Being Rolled Back while addingAgent.......");
			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				// sessionObj.close();
			}

		}
		return agent_obj;

	}

	public CallbackRequests placeCallBackRequest(Timestamp requestTime, Timestamp callbackTime, String status,
			Customer customer) {

		System.out.println(".......Placing the callback request.......\n");

		CallbackRequests callbackRequests = null;
		try {
			boolean isEligibleToAdd = true;
			Query query = sessionObj.createQuery("from CallbackRequests");
			List<CallbackRequests> cbrList = query.list();

			for (CallbackRequests cbr : cbrList) {

				if (cbr.getCustomer().getCustomer_phone().equalsIgnoreCase(customer.getCustomer_phone())) {

					if (cbr.getStatus().equalsIgnoreCase("pending")) {
						isEligibleToAdd = false;
					}
				}
			}

			if (isEligibleToAdd) {
				callbackRequests = new CallbackRequests(requestTime, callbackTime, status);
				callbackRequests.setCustomer(customer);
				sessionObj.save(callbackRequests);
				sessionObj.getTransaction().commit();
			} else {
				System.out.println("this customer is already requseted for callback.. ");
			}
		} catch (Exception sqlException) {

			System.out.println("\n.......Transaction Is Being Rolled Back while placeCallBackRequest.......");
			sqlException.printStackTrace();

		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}

		}
		return callbackRequests;
	}

	public CallbackRequests updatingAgentIDInCallBackRequest(Agent agent, CallbackRequests callbackRequests) {
		System.out.println(".......Updating the agent details in CallBackRequest table.......\n");
		try {

			callbackRequests.setAgent(agent);
			sessionObj.update(callbackRequests);
			sessionObj.getTransaction().commit();

		} catch (Exception sqlException) {

			System.out
					.println("\n.......Transaction Is Being Rolled Back while updatingAgentIDInCallBackRequest.......");
			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}

		}
		return callbackRequests;
	}

	public CallbackLogs updatingTheLogs(String log_time, String logMessage, CallbackRequests callbackRequests) {
		System.out.println(".......Updating the Log table.......\n");
		CallbackLogs callbackLogs = null;
		try {
			sessionObj = getSession();
			sessionObj.beginTransaction();
			callbackLogs = new CallbackLogs(log_time, logMessage);
			callbackLogs.setCallbackRequests(callbackRequests);
			sessionObj.save(callbackLogs);
			sessionObj.getTransaction().commit();
		} catch (Exception sqlException) {

			System.out.println("\n.......Transaction Is Being Rolled Back while updatingTheLogs.......");

			sqlException.printStackTrace();
		} finally {
			if (sessionObj != null) {
				sessionObj.close();
			}

		}
		return callbackLogs;
	}
}
