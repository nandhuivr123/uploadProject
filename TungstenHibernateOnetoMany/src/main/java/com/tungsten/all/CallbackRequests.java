package com.tungsten.all;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "callbackRequests")
public class CallbackRequests {
	
	

	@Id
	@GeneratedValue
	@Column(name = "request_id")
	private Long request_id;
	
	//@Column(name = "request_time")
	//private String request_time;
	
	@Column(name = "request_time")
	private Timestamp request_time;
	
	//@Column(name = "callback_time")
	//private String callback_time;
	
	@Column(name = "callback_time")
	private Timestamp callback_time;
	
	@Column(name = "status")
	private String status;
	
	@ManyToOne
	@JoinColumn(name = "cust_id")
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@OneToOne(mappedBy = "callbackRequests", cascade = CascadeType.ALL)
    private CallbackLogs callbacklogs;	
	
	public CallbackRequests() {
		super();
	}
	public CallbackRequests(Timestamp request_time, Timestamp callback_time,
			String status) {
		super();
		this.request_time = request_time;
		this.callback_time = callback_time;
		this.status = status;
	}
	public Long getRequest_id() {
		return request_id;
	}
	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	/*
	 * public String getRequest_time() { return request_time; } public void
	 * setRequest_time(String request_time) { this.request_time = request_time; }
	 */
	/*
	 * public String getCallback_time() { return callback_time; } public void
	 * setCallback_time(String callback_time) { this.callback_time = callback_time;
	 * }
	 */
	
	public String getStatus() {
		return status;
	}
	public Timestamp getRequest_time() {
		return request_time;
	}
	public void setRequest_time(Timestamp request_time) {
		this.request_time = request_time;
	}
	public Timestamp getCallback_time() {
		return callback_time;
	}
	public void setCallback_time(Timestamp callback_time) {
		this.callback_time = callback_time;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	
	public CallbackLogs getCallbacklogs() {
		return callbacklogs;
	}
	public void setCallbacklogs(CallbackLogs callbacklogs) {
		this.callbacklogs = callbacklogs;
	}
	@Override
	public String toString() {
		return "CallbackRequests [request_id=" + request_id
				+ ", request_time=" + request_time + ", callback_time=" + callback_time + ", status=" + status +
				 "]";	}
	
	
}
