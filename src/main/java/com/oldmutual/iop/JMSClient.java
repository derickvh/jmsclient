package com.oldmutual.iop;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class JMSClient {

	/**
	 * Empty constructor
	 */

	public JMSClient() {

	}

	private QueueConnection initJMS(MQQueueConnectionFactory qcf, JMSConfiguration jmsConfig) throws JMSException {

		qcf.setHostName(jmsConfig.getHostname());
		qcf.setPort(jmsConfig.getPort());
		qcf.setQueueManager(jmsConfig.getQmanager());
		qcf.setChannel(jmsConfig.getChannel());
		qcf.setTransportType(WMQConstants.WMQ_CM_CLIENT);

		return qcf.createQueueConnection(jmsConfig.getUserID(), jmsConfig.getPasswd());
	}

	/**
	 * Reads a message from a JMS destination.
	 * 
	 * @param jmsConfig
	 *            An instance of the Bean/Data class JMSConfiguration
	 * @return Returns the body of the message, which is expected to be of type
	 *         'TextMessage'.
	 * @throws JMSException
	 */

	public String read(JMSConfiguration jmsConfig) throws JMSException {

		QueueConnection queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig);

		queueConnection.start();

		Queue queue = new MQQueue(jmsConfig.getDestination());
		QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueReceiver queueRcvr = session.createReceiver(queue);

		Message msg = queueRcvr.receive(5L);
		String returnString = ((TextMessage) msg).getText();

		queueConnection.close();
		
		return returnString != null ?  returnString : "No message or wrong message type.";
	}

	public boolean write(JMSConfiguration jmsConfig, String text) {

		boolean returnBool = true;
		QueueConnection queueConnection = null;

		try {

			queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig);
			queueConnection.start();

			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			QueueSender qSender = session.createSender(queue);
			Message msg = session.createTextMessage(text);
			qSender.send(msg);

		} catch (JMSException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnBool = false;
			
		} finally {

			if (queueConnection != null) {

				try {
					queueConnection.close();
					
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return returnBool;
	}
}
