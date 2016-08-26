package com.oldmutual.iop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oldmutual.iop.JMSClient;
import com.oldmutual.iop.JMSConfiguration;

public class JMSClientTest {

	@Test
	public void test() {

		String textMsg = "The quick brown fox jumps over the lazy dog.";
		
		JMSConfiguration jmsC = new JMSConfiguration();
		
		jmsC.setChannel("SERVCONN");
		jmsC.setDestination("JMSQ1");
		jmsC.setHostname("localhost");
		jmsC.setQmanager("QMSPINOZA");
		jmsC.setPort(1415);
		jmsC.setUserID("derick");
		jmsC.setPasswd("nopasswd");
		
		JMSClient jmsClient = new JMSClient();
		
		assertTrue(jmsClient.write(jmsC, textMsg));
		
		System.out.print(textMsg);
		System.out.println(" sent successfully");
		
		String returnTxt = null;
		
		returnTxt = jmsClient.read(jmsC);
				
		assertTrue(returnTxt.startsWith(textMsg));
		
		System.out.print(returnTxt);
		System.out.println(" read successfully");
	}
}

