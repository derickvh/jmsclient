
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

import java.util.ArrayList;
import java.util.List;

public class JMSClient {

	/**
	 * Empty constructor
	 */

	public JMSClient() {

	}

	/**
	 * Initialises JMS objects and returns a QueueConnection object
	 *
	 * @param qcf
	 *            MQQueueConnectionFactory
	 * @param jmsConfig
	 *            A JMSConfiguration object
	 * @return A queue connection object
	 * @throws JMSException
     * Throws a JMS exception.
	 */

	private static QueueConnection initJMS(MQQueueConnectionFactory qcf, JMSConfiguration jmsConfig)
			throws JMSException {

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
	 *         'TextMessage'. Waits for 5 seconds for a message before
	 *         returning.
	 *
	 */

	public static String read(JMSConfiguration jmsConfig) {

		String returnString = null;

		try (QueueConnection qConn = initJMS(new MQQueueConnectionFactory(), jmsConfig)) {

			qConn.start();

			QueueSession session = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueReceiver queueRcvr = session.createReceiver(queue);

			Message msg = queueRcvr.receive(jmsConfig.getTimeout());

            if (msg != null) {

                returnString = ((TextMessage) msg).getText();

            } else {

                returnString = "No Message Read";
            }

		} catch (JMSException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return returnString;
	}

	/**
	 * Reads multiple messages from a JMS destination.
	 *
	 * @param jmsConfig
	 *            Instance of a JMS Configuration bean.
	 * @return A list of messages read.
	 */

	public static List<String> readMany(JMSConfiguration jmsConfig) {

		List<String> msgs = new ArrayList<>();

		try (QueueConnection queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig)) {

			queueConnection.start();

			QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueReceiver queueRcvr = session.createReceiver(queue);

			Message msg;

			while ((msg = queueRcvr.receiveNoWait()) != null) {

				msgs.add(((TextMessage) msg).getText());
			}

		} catch (JMSException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgs;
	}

	/**
	 * Writes a message to a JMS destination.
	 *
	 * @param jmsConfig
	 *            A configuration object that contains all the details for
	 *            connecting to a JMS destination
	 * @param text
	 *            The message that must be sent to the JMS destination. @return.
	 *            Returns true if the message was successfully transmitted.
	 */
	public static boolean write(JMSConfiguration jmsConfig, String text) {

		boolean returnBool = true;

		try (QueueConnection queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig)) {

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

		}
		return returnBool;
	}

	/**
	 * Writes multiple messages to a JMS destination.
	 *
	 * @param jmsConfig
	 *            A configuration object that contains all the details for
	 *            connecting to a JMS destination
	 * @param msgs
	 *            An array of messages that must be sent to the JMS destination.
	 *
	 * 			@return Returns true if the messages were successfully
	 *            transmitted.
	 *
	 **/

	public static boolean writeMany(JMSConfiguration jmsConfig, List<String> msgs) {

		boolean returnBool = true;

		try (QueueConnection queueConnection = initJMS(new MQQueueConnectionFactory(), jmsConfig)) {

			queueConnection.start();

			QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueSender qSender = session.createSender(queue);

			Message msg;

			for (String msgTxt : msgs) {

				msg = session.createTextMessage(msgTxt);
				qSender.send(msg);
			}

		} catch (JMSException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			returnBool = false;
		}
		return returnBool;
	}
}
