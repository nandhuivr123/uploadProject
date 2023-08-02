package com.tungsten.all;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "customer")
public class Customer {
	
	
	@Id
	@GeneratedValue
	@Column(name = "cust_id")
	private long id;
	
	@Column(name = "customer_name")
	private String customer_name;
	
	@Column(name = "customer_phone")
	private String customer_phone;
	
	@Column(name = "customer_email")
	private String customer_email;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<CallbackRequests> callbackRequests;

	public Customer() {
		super();
	}
	public Customer(String customer_name, String customer_phone, String customer_email) {
		super();
		this.customer_name = customer_name;
		this.customer_phone = customer_phone;
		this.customer_email = customer_email;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_phone() {
		return customer_phone;
	}
	public void setCustomer_phone(String customer_phone) {
		this.customer_phone = customer_phone;
	}
	public String getCustomer_email() {
		return customer_email;
	}
	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}
	public Set<CallbackRequests> getCallbackRequests() {
		return callbackRequests;
	}
	public void setCallbackRequests(Set<CallbackRequests> callbackRequests) {
		this.callbackRequests = callbackRequests;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", customer_name=" + customer_name + ", customer_phone=" + customer_phone
				+ ", customer_email=" + customer_email + "]";
	}
}
