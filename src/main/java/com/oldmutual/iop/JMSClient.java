
package com.oldmutual.iop;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Integer.valueOf;
import static java.lang.Long.parseLong;

/**
 * A JMS client with IBM MQ the JMS provider.
 * The class has a number of static methods for reading and writing to JMS destinations.
 * There are methods for sending and receiving single messages; multiple messages; messages read from a directory;
 * messages written as text files to a directory.
 *
 *
 * To initialise the JMS configuration either a properties file or a configuration bean, called JMSConfiguration,
 * can be used.
 *
 * The properties file is only used when the main method of the JMSClient class is called. When the class is
 * used as a library an instance of the JMSConfiguration class must be used to initialise the JMS/MQ configuration.
 *
 */

public class JMSClient {

    private static JMSConfiguration jmsC = new JMSConfiguration();

    /**
     * The main entry point to executing the methods to read messages from a file system directory
     * or write messages read to a specified directory as text files.
     *
     * @param args the first parameter is either -s or -r (send or receive); the second
     *             is the directory from which message files are read, or to which messages are to be
     *             written as files. The third parameter is the name of a properties file for configuring
     *             the IBM JMS/MQ environment.
     *             <p>
     *             The properties file must be in the current directory and have the following
     *             key/value pairs. Adjust to suit the MQ environment to be used:
     *             </p>
     *             <p>
     *             host=localhost
     *             port=1415
     *             channel=SERVCONN
     *             qmanager=QMGR1
     *             destination=queue1
     *             userID=mq_user
     *             passwd=mq_passwd
     *             timeout=5 (secs)
     *             </p>
     */

    public static void main(String args[]) {

        if (args.length < 3) {
            usage();
            System.exit(0);
        }

        if (!readJMSConfiguration(args[2])) {

            System.out.println("Failed to read JMS Configuration properties files.");
            System.exit(3);
        }

        boolean rc = true;

        switch (args[0]) {

            case "-s":
                rc = sendFiles(args[1]);
                break;

            case "-r":
                rc = readMsgs(args[1]);
                break;

            default:
                usage();
        }

        if (!rc) {
            System.exit(2);
        } else {
            System.exit(0);
        }
    }

    private static void usage() {

        System.out.println("JMSClient usage:");
        System.out.println("Send files as text messages: jmsclient -s <srcDir> <JMS configuration properties file>");
        System.out.println("Receive text messages and save them as files: jmsclient -r <toDir> <JMS configuration properties file>");
    }

    private static boolean readJMSConfiguration(String filename) {

        Properties jmsProps = readPropsFile(filename);

        if (!jmsProps.isEmpty()) {

            jmsC.setHostname(jmsProps.getProperty("hostname", "localhost"));
            jmsC.setPort(valueOf(jmsProps.getProperty("port", "1415")));
            jmsC.setQmanager(jmsProps.getProperty("qmanager"));
            jmsC.setChannel(jmsProps.getProperty("channel"));
            jmsC.setDestination(jmsProps.getProperty("destination"));
            jmsC.setUserID(jmsProps.getProperty("userID"));
            jmsC.setPasswd(jmsProps.getProperty("passwd"));
            jmsC.setTimeout(parseLong(jmsProps.getProperty("timeout", "1")));

            return true;

        } else {

            return false;
        }
    }

    /**
     * Sends the files in the specified directory as text messages to the specified JMS destination.
     *
     * @param srcDir The directory where the text files are located.
     * @return Returns true if the operation completed successfully; else false.
     */

    public static boolean sendFiles(String srcDir) {

        List<String> msgs = readFiles(srcDir);

        return !msgs.isEmpty() && writeMany(msgs);
    }

    /**
     * Sends the files in the specified directory as text messages to the specified JMS destination
     *
     * @param jmsConfig An instance of a JMS configuration bean
     * @param srcDir    The directory where text files are located.
     * @return True if the operation completed successfully; else false.
     */
    public static boolean sendFiles(JMSConfiguration jmsConfig, String srcDir) {

        List<String> msgs = readFiles(srcDir);

        return !msgs.isEmpty() && writeMany(jmsConfig, msgs);
    }

    private static List<String> readFiles(String srcDir) {

        Path dir = Paths.get(srcDir);
        List<String> msgs = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            for (Path entry : stream) {
                msgs.add(new String(Files.readAllBytes(Paths.get(entry.toString()))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return msgs;
    }

    /**
     * Reads messages from the specified JMS destination and save them as text files to the specified directory.
     * @param toDir The directory to which the text files are to be written.
     * @return True if the operation completed successfully; false otherwise.
     */

    public static boolean readMsgs(String toDir) {

         return writeMsgsToFile(toDir, readMany());
    }

    /**
     * Reads messages from the specified JMS destination and save them as text files to the specified directory.
     * @param jmsConfig An instance on a JMS Configuration bean
     * @param toDir The directory to which the text files are to be written.
     * @return True if the operation completed successfully; false otherwise.
     */

    public static boolean readMsgs(JMSConfiguration jmsConfig, String toDir) {

        return writeMsgsToFile(toDir, readMany(jmsConfig));
    }

    private static boolean writeMsgsToFile(String toDir, List<String>msgs) {

        boolean rval = true;

        if (msgs.isEmpty()) {

            System.out.println("No message files written ...");

        } else {

            int i = 0;

            for (String msg : msgs) {

                StringBuilder sb = new StringBuilder(toDir);

                sb.append(File.separator);
                sb.append("msg");
                sb.append(Integer.valueOf(++i).toString());
                sb.append(".txt");

                try {

                    Files.write(Paths.get(sb.toString()), msg.getBytes());

                } catch (IOException e) {

                    e.printStackTrace();
                    rval = false;
                }
            }
            System.out.println("Wrote messages to directory " + toDir);
        }
        return rval;
    }

    /**
	 * Initialises JMS objects and returns a QueueConnection object
	 *
	 * @param jmsConfig
	 *            A JMSConfiguration object
	 * @return A queue connection object
	 * @throws JMSException
     * Throws a JMS exception.
	 */

	private static QueueConnection initJMS(JMSConfiguration jmsConfig)
			throws JMSException {

        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();

        mqQueueConnectionFactory.setHostName(jmsConfig.getHostname());
        mqQueueConnectionFactory.setPort(jmsConfig.getPort());
        mqQueueConnectionFactory.setQueueManager(jmsConfig.getQmanager());
        mqQueueConnectionFactory.setChannel(jmsConfig.getChannel());
        mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

		return mqQueueConnectionFactory.createQueueConnection(jmsConfig.getUserID(), jmsConfig.getPasswd());
	}

	/**
	 * Reads a single message from a JMS destination.
	 *
	 * @param jmsConfig
	 *            An instance of the Bean/Data class JMSConfiguration.
	 * @return Returns the body of the message, which is expected to be of type
	 *         'TextMessage'. Waits for specified number of seconds for a message to arrive before
	 *         returning.
	 *
	 */

	public static String read(JMSConfiguration jmsConfig) {

		String returnString = null;

		try (QueueConnection qConn = initJMS(jmsConfig)) {

			qConn.start();

			QueueSession session = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = new MQQueue(jmsConfig.getDestination());
			QueueReceiver queueRcvr = session.createReceiver(queue);

			Message msg = queueRcvr.receive(jmsConfig.getTimeout());

            if (msg != null) {

                returnString = ((TextMessage) msg).getText();

            } else {

                returnString = "No message read.";
            }

		} catch (JMSException e) {

			e.printStackTrace();

		}
		return returnString;
	}

	/**
	 * Reads multiple messages from a JMS destination. Does not wait for messages to arrive. Terminates as soon
     * it has read the last available message.
	 *
	 * @param jmsConfig
	 *            Instance of a JMS Configuration bean.
	 * @return A list of messages read.
	 */

	public static List<String> readMany(JMSConfiguration jmsConfig) {

        jmsC = jmsConfig;

        return readMany();
	}

    /**
     * Reads multiple messages from a JMS destination. Does not wait for messages to arrive. Terminates as soon
     * it has read the last available message.
     * @return A list of messages read.
     */

	public static List<String> readMany() {

        List<java.lang.String> msgs = new ArrayList<>();

        try (QueueConnection queueConnection = initJMS(jmsC)) {

            queueConnection.start();

            QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = new MQQueue(jmsC.getDestination());
            QueueReceiver queueRcvr = session.createReceiver(queue);

            Message msg;

            while ((msg = queueRcvr.receiveNoWait()) != null) {

                msgs.add(((TextMessage) msg).getText());
            }
        } catch (JMSException e) {

            e.printStackTrace();
        }
        return msgs;
    }

	/**
	 * Writes a message to a JMS destination.
	 *
	 * @param jmsConfig
	 *            A configuration bean that contains all the details for
	 *            connecting to a JMS destination
	 * @param text
	 *            The message that must be sent to the JMS destination.
     * @return True if the message was successfully transmitted.
	 */
	public static boolean write(JMSConfiguration jmsConfig, String text) {

		boolean returnBool = true;

		try (QueueConnection queueConnection = initJMS(jmsConfig)) {

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
	 *            A configuration object bean that contains all the details for
	 *            connecting to a JMS destination.
	 * @param msgs
	 *            A list of messages that must be sent to the JMS destination.
	 * @return True if the messages were successfully transmitted.
	 *
	 *
	 **/

	public static boolean writeMany(JMSConfiguration jmsConfig, List<String> msgs) {

        jmsC = jmsConfig;

        return writeMany(msgs);
	}

    /**
     * Writes multiple messages to a JMS destination.
     * @param msgs A list of messages that must be sent to the JMS destination.
     * @return True if the messages were successfully transmitted.
     *
     */

	public static boolean writeMany(List<String> msgs) {

        boolean returnBool = true;

        try (QueueConnection queueConnection = initJMS(jmsC)) {

            queueConnection.start();

            QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = new MQQueue(jmsC.getDestination());
            QueueSender qSender = session.createSender(queue);

            Message msg;

            System.out.println("Sending " + msgs.size() + " messages.");

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

    /**
     * Reads JMS configuration properties from a properties file. By convention it is named, jmsconfig.properties
     * @param fProps The name of the properties file.
     * @return A java.util.Properties object populated with properties read from the properties file.
     */

    static Properties readPropsFile(String fProps) {

        Properties prop = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(fProps);

        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
