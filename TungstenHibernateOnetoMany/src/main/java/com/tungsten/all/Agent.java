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
@Table(name = "agent")
public class Agent {
	
	
@Id
@GeneratedValue
@Column(name = "agent_id")
private Long agent_id;

@Column(name = "agent_name")
private String agent_name;

@Column(name = "agent_phone")
private String agent_phone;

@Column(name = "agent_email")
private String agent_email;


@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
private Set<CallbackRequests> callbackRequests;


public Agent() {
	super();
	// TODO Auto-generated constructor stub
}


public Agent(String agent_name, String agent_phone, String agent_email
		) {
	super();
	this.agent_name = agent_name;
	this.agent_phone = agent_phone;
	this.agent_email = agent_email;
}


public Long getAgent_id() {
	return agent_id;
}


public void setAgent_id(Long agent_id) {
	this.agent_id = agent_id;
}


public String getAgent_name() {
	return agent_name;
}


public void setAgent_name(String agent_name) {
	this.agent_name = agent_name;
}


public String getAgent_phone() {
	return agent_phone;
}


public void setAgent_phone(String agent_phone) {
	this.agent_phone = agent_phone;
}


public String getAgent_email() {
	return agent_email;
}


public void setAgent_email(String agent_email) {
	this.agent_email = agent_email;
}


public Set<CallbackRequests> getCallbackRequests() {
	return callbackRequests;
}


public void setCallbackRequests(Set<CallbackRequests> callbackRequests) {
	this.callbackRequests = callbackRequests;
}


@Override
public String toString() {
	return "Agent [agent_id=" + agent_id + ", agent_name=" + agent_name + ", agent_phone=" + agent_phone
			+ ", agent_email=" + agent_email + "]";
}




}
