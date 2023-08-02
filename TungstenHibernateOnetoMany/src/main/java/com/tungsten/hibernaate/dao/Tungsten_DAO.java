package com.tungsten.hibernaate.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tungsten.HibernateUtil.HibernateUtil;
import com.tungsten.all.Agent;
import com.tungsten.all.App;
import com.tungsten.all.CallbackLogs;
import com.tungsten.all.CallbackRequests;
import com.tungsten.all.Customer;

public class Tungsten_DAO {
	private static Logger logger=  LogManager.getLogger(Tungsten_DAO.class);

	public Customer addingCustomer(String name, String phone_no, String mail) {
		System.out.println(".......Adding the customer.......\n");
		logger.info(".......Adding the customer.......\n");
		Session session = null;
		Transaction transaction = null;
		Customer cus_obj = null;
		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			cus_obj = new Customer(name,phone_no,mail);

			session.save(cus_obj);

			System.out.println("inserted customer: "+cus_obj.getCustomer_name());
			logger.info("inserted customer: "+cus_obj.getCustomer_name());

			logger.info("customer details: "+cus_obj.toString());

			transaction.commit();

		} catch(Exception ex) {

			logger.error("Exception while adding the customer :"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while adding the customer..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return cus_obj;
	}
	public CallbackRequests placeCallBackRequest(Timestamp requestTime,Timestamp callbackTime,String status, Customer customer) {
		System.out.println(".......Placing the callback request.......\n");

		Session session = null;
		Transaction transaction = null;
		CallbackRequests callbackRequests = null;
		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			boolean isEligibleToAdd=true;
			Query query = session.createQuery("from CallbackRequests");
			List<CallbackRequests> cbrList = query.list();

			logger.info("This customer phone number is:"+customer.getCustomer_phone());

			for(CallbackRequests db : cbrList){

				logger.info("Customer phone number from DB is:"+db.getCustomer().getCustomer_phone());

				if(db.getCustomer().getCustomer_phone().equalsIgnoreCase(customer.getCustomer_phone())) {

					logger.info("Customer phone number status for call back request is:"+db.getStatus());

					if(db.getStatus().equalsIgnoreCase("pending")) {

						logger.info("This customer is eligible to place call back request..");
						isEligibleToAdd=false;
					}
				}
			}

			if(isEligibleToAdd) {
				callbackRequests = new CallbackRequests(requestTime,callbackTime,status);
				callbackRequests.setCustomer(customer);
				session.save(callbackRequests);
				transaction.commit();
			}
			else {
				System.out.println("this customer is already requseted for callback.. ");

				logger.info("this customer is already requseted for callback.. so deleting this duplicate customer detail from customet table..");

				HibernateUtil.closeSession();
				deleteCustomer(customer);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error("Exception while placeCallBackRequest:"+ex.getMessage());
			if(transaction != null) {
				logger.debug("Transaction being rollback while placeCallBackRequest..");
				transaction.rollback();
			}

		} 
		finally {
			HibernateUtil.closeSession();
		}
		return callbackRequests;
	}
	public Agent addingAgent(String agentName, String agentPhoneno, String agentMailId) {

		System.out.println(".......Adding the agents.......\n");
		logger.info(".......Adding the agents.......\n");

		Session session = null;
		Transaction transaction = null;
		Agent agent_obj = null;

		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			agent_obj = new Agent(agentName,agentPhoneno,agentMailId);
			session.save(agent_obj);

			logger.info("inserted agent name: "+agent_obj.getAgent_name());

			logger.info("Agent details: "+agent_obj.toString());

			transaction.commit();

		} catch(Exception ex) {
			logger.error("Exception while addingAgent:"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while addingAgent..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return agent_obj;

	}
	public CallbackRequests updatingAgentIDInCallBackRequest(Agent agent,CallbackRequests callbackRequests) {
		System.out.println(".......Updating the agent details in CallBackRequest table.......\n");
		logger.info(".......Updating the agent details in CallBackRequest table.......\n");


		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			logger.info("Agent id :"+agent.getAgent_id()+"Callback request ID:"+callbackRequests.getRequest_id());

			callbackRequests.setAgent(agent);

			session.update(callbackRequests);
			transaction.commit();

		} catch(Exception ex) {
			logger.error("Exception while updatingAgentIDInCallBackRequest:"+ex.getMessage());

			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while updatingAgentIDInCallBackRequest..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return callbackRequests;
	}
	public CallbackLogs updatingTheLogs(String log_time,String logMessage,CallbackRequests callbackRequests) {

		System.out.println(".......Updating the Log table.......\n");
		logger.info(".......Updating the Log table.......\n");


		CallbackLogs callbackLogs = null;
		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			callbackLogs = new CallbackLogs(log_time,logMessage);
			callbackLogs.setCallbackRequests(callbackRequests);

			session.save(callbackLogs);

			logger.info("Logs are added.. log id is:"+callbackLogs.getLog_id());

			transaction.commit();

		}catch(Exception ex) {

			logger.error("Exception while updatingTheLogs:"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while updatingTheLogs..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return callbackLogs;
	}

	public void deleteCustomer(Customer customer) {
		System.out.println("....Deleting the Duplicated customer...");
		logger.info("....Deleting the Duplicated customer...");

		Session session = null;
		Transaction transaction = null;

		try {

			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();
			session.delete(customer);
			transaction.commit();

			logger.info("deleted customer since request is already placed: "+customer.getCustomer_name());

			System.out.println("deleted customer since request is already placed: "+customer.getCustomer_name());
		} 
		catch(Exception ex) {

			logger.error("Exception while deleteCustomer:"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while deleteCustomer..");
				transaction.rollback();
			}
		} finally {
			HibernateUtil.closeSession();
		}
	}
	public CallbackRequests getCallBackRequestObjectByRequestID(Long reqid){


		logger.info("************getCallBackRequestObjectByRequestID**********");

		Session session = null;
		CallbackRequests cbr = null;
		try {
			session = HibernateUtil.openCurrentSession();
			String queryStr = "select cbr from CallbackRequests cbr";
			cbr = (CallbackRequests) session.get(CallbackRequests.class, reqid);

			logger.info("CallbackRequest details for the "+reqid+ "is "+ cbr.toString());

		} catch(Exception ex) {
			logger.error("Exception while getCallBackRequestObjectByRequestID:"+ex.getMessage());
			ex.printStackTrace();

		} finally {
			HibernateUtil.closeSession();
		}
		return cbr;
	}

	public CallbackRequests updateTheStatusByAgent(CallbackRequests callbackRequests, String status) {
		System.out.println(".......Updating the status in CallBackRequest table by Agent.......\n");
		logger.info("......Updating the status in CallBackRequest table by Agent.......\n");

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			callbackRequests.setStatus(status);

			session.update(callbackRequests);

			logger.info("Status updated in table:"+callbackRequests.getStatus());

			transaction.commit();

		} catch(Exception ex) {
			logger.error("Exception while updateTheStatusByAgent:"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while updateTheStatusByAgent..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return callbackRequests;

	}
	public Customer updateNewNumberForCallback(Customer cust,String phoneno) {
		System.out.println(".......Updating the New number in Customer table.......\n");
		logger.info(".......Updating the New number in Customer table.......\n");

		Session session = null;
		Transaction transaction = null;

		try {
			session = HibernateUtil.openCurrentSession();
			transaction = session.beginTransaction();

			cust.setCustomer_phone(phoneno);

			session.update(cust);

			logger.info("Status updated in table:"+cust.getCustomer_phone());

			transaction.commit();

		} catch(Exception ex) {
			logger.error("Exception while updateNewNumberForCallback:"+ex.getMessage());
			ex.printStackTrace();

			if(transaction != null) {
				logger.debug("Transaction being rollback while updateNewNumberForCallback..");
				transaction.rollback();
			}
		} 
		finally {
			HibernateUtil.closeSession();
		}
		return cust;

	}
}
