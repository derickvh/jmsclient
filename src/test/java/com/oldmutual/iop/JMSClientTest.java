package com.oldmutual.iop.test;

import javax.jms.JMSException;

import com.oldmutual.iop.JMSClient;
import com.oldmutual.iop.JMSConfiguration;

public class JMSClientTest {

	public static void main(String[] args) throws JMSException {
		
		String textMsg = args[0];
		
		JMSConfiguration jmsC = new JMSConfiguration();
		
		jmsC.setChannel("SERVCONN");
		jmsC.setDestination("JMSQ1");
		jmsC.setHostname("localhost");
		jmsC.setQmanager("QMSPINOZA");
		jmsC.setPort(1415);
		jmsC.setUserID("derick");
		jmsC.setPasswd("nopasswd");
		
		JMSClient jmsClient = new JMSClient();
		
		org.junit.Assert.assertTrue(jmsClient.write(jmsC, textMsg));
		
		System.out.print(textMsg);
		System.out.println(" sent successfully");
		
		String returnTxt = jmsClient.read(jmsC);
				
		org.junit.Assert.assertTrue(returnTxt.startsWith(textMsg));
		
		System.out.print(returnTxt);
		System.out.println(" read successfully");
	}
}
