package com.tungsten.all;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "callbackLogs")
public class CallbackLogs {
	
@Id
@GeneratedValue
@Column(name = "log_id")
private Long log_id;

@Column(name = "log_time")
private String log_time;

@Column(name = "log_message")
private String log_message;

@OneToOne
@JoinColumn(name = "request_id")
private CallbackRequests callbackRequests;
 

public CallbackLogs() {
	super();
	// TODO Auto-generated constructor stub
}


public CallbackLogs(String log_time, String log_message) {
	super();
	this.log_time = log_time;
	this.log_message = log_message;
}


public Long getLog_id() {
	return log_id;
}
public void setLog_id(Long log_id) {
	this.log_id = log_id;
}

public String getLog_time() {
	return log_time;
}
public void setLog_time(String log_time) {
	this.log_time = log_time;
}
public String getLog_message() {
	return log_message;
}
public void setLog_message(String log_message) {
	this.log_message = log_message;
}
public CallbackRequests getCallbackRequests() {
	return callbackRequests;
}
public void setCallbackRequests(CallbackRequests callbackRequests) {
	this.callbackRequests = callbackRequests;
}
@Override
public String toString() {
	return "CallbackLogs [log_id=" + log_id + ", log_time=" + log_time + ", log_message="
			+ log_message + "]";
}




}
