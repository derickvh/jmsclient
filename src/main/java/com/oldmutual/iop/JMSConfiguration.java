package com.oldmutual.iop;

public class JMSConfiguration {
	
	private String hostname;
	private int port;
	private String qmanager;
	private String channel;
	private String userID;
	private String passwd;
	private String destination;
	
	public JMSConfiguration() {
		
		//Empty, no-action constructor
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getQmanager() {
		return qmanager;
	}
	public void setQmanager(String qmanager) {
		this.qmanager = qmanager;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	

}
