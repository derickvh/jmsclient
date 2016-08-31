package com.oldmutual.iop;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="JMSConfiguration", namespace="##default")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
    "hostname",
    "port",
    "qmanager",
    "channel",
    "userID",
    "passwd",
    "destination"
})

public class JMSConfiguration {
	
	@XmlElement(name="hostname", required=true)
	private String hostname;
	@XmlElement(name="port", required=false)
	private int port;
	@XmlElement(name="qmanager", required=true)
	private String qmanager;
	@XmlElement(name="channel", required=true)
	private String channel;
	@XmlElement(name="userID", required=false)
	private String userID;
	@XmlElement(name="passwd", required=false)
	private String passwd;
	@XmlElement(name="destination", required=true)
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
