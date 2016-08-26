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
	 *            An instance of the Bean/Data class JMSConfiguration.
	 * @return Returns the body of the message, which is expected to be of type
	 *         'TextMessage'. Waits for 5 seconds for a message before returning.
	 * 
	 */

	public String read(JMSConfiguration jmsConfig) {

		QueueConnection queueConnection = null;
		String returnString = null;

		try {

			queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig);
			queueConnection.start();

			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueReceiver queueRcvr = session.createReceiver(queue);

			Message msg = queueRcvr.receive(5L);
			
			returnString = ((TextMessage) msg).getText();

		} catch (JMSException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			try {

				queueConnection.close();

			} catch (JMSException e) {

				System.out.println("Failed to close JMS Queue Connection.");
				e.printStackTrace();
			}
		}
		return returnString != null ? returnString : "No message or wrong message type.";
	}

	/**
	 * Writes a message to a JMS destination.
	 * 
	 * @param jmsConfig.
	 *            A configuration object that contains all the details for
	 *            connecting to a JMS destination
	 * @param text.
	 *            The message that must be sent to the JMS destination. @return.
	 *            Returns true if the message was successfully transmitted.
	 */
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

