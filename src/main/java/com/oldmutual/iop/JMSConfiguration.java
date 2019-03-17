package com.oldmutual.iop;

/**
 * A Java Bean that contains information to configure an IBM MQ environment to provide JMS functionality.
 */


public class JMSConfiguration {

    private String hostname;
    private int port;
    private String qmanager;
    private String channel;
    private String userID;
    private String passwd;
    private String destination;
    private long timeout;

    public JMSConfiguration() {

        //Empty, no-action constructor
    }

    /**
     * Gets the hostname of the server on which the MQ QM manager that provides the JMS functionality is located.
     * @return The fully qualified domain name, simple name (e.g. 'localhost') or
     * IP address of the QM manager host server.
     */

    public String getHostname() {

        return hostname;
    }

    /**
     * Sets the hostname of the server of the QM manager that provides the JMS functionality.
     * @param hostname A FQDN, or simple host name (e.g. 'localhost'), or IP address.
     */
    public void setHostname(String hostname) {

        this.hostname = hostname;
    }

    /**
     * Gets the TCP/IP port on which the MQ Listener is listening for incoming connections.
     * @return By convention 1414, but could be anything else.
     */

    public int getPort() {

        return port;
    }

    /**
     * Sets the port on which the MQ Listener is listening for incoming connections.
     * @param port An integer value.
     */
    public void setPort(int port) {

        this.port = port;
    }

    /**
     * Gets the name of the MQ Queue Manager that provides JMS services.
     *
     * @return The name of the MQ Queue Manager
     */

    public String getQmanager() {

        return qmanager;
    }

    /**
     * Sets the name of the IBM Queue Manager
     * @param qmanager Name, e.g. 'QMSPINOZA'
     */

    public void setQmanager(String qmanager) {

        this.qmanager = qmanager;
    }

    /**
     * Gets the MQ server connection channel to be used to connect to the MQ Queue Manager.
     * @return The name of the Server Connection Channel
     */

    public String getChannel() {

        return channel;
    }

    /**
     * Sets the name of the MQ server connection channel to be used to connect to the Queue Manager,
     * @param channel The name of the server connection channel, (e.g 'SERVERCONN').
     */

    public void setChannel(String channel) {

        this.channel = channel;
    }

    /**
     * Gets the user ID of the connecting client that wants access to a particular JMS destination (MQ queue).
     * @return The userID of the connecting client, e.g. 'jms_user'
     */

    public String getUserID() {

        return userID;
    }

    /**
     * Sets the user ID of the connecting client that wants access to a particular JMS destination (MQ queue).
     * @param userID The userID (operating system, or Active Directort username).
     */

    public void setUserID(String userID) {

        this.userID = userID;
    }

    /**
     * Gets the password of the connecting client user ID.
     * @return The password of the user ID under which the connecting client is running.
     */

    public String getPasswd() {

        return passwd;
    }

    /**
     * Sets the password of the connecting client user ID. Whether the password is used depends on security
     * settings of IBM MQ V8.0 and higher. See MQ CONNAUTH and related security properties.
     * @param passwd The password of the user ID.
     */
    public void setPasswd(String passwd) {

        this.passwd = passwd;
    }

    /**
     * Gets the JMS destination (IBM MQ queue). Typically a local queue on the specified MQ queue manager.
     * @return JMS destination
     */
    public String getDestination() {

        return destination;
    }

    /**
     * Sets the JMS destination (IBM MQ queue). Typically a local queue on the specified MQ queue manager.
     * @param destination The name of the JMS destination/MQ queue.
     */
    public void setDestination(String destination) {

        this.destination = destination;
    }

    /**
     * Gets the time in seconds that a JMS receiver will wait for a message to arrive on a JMS destination.
     * @return  long type. The time in seconds.
     */

    public long getTimeout() {

        return timeout;
    }

    /**
     * Sets the time in seconds that a receiver will wait for a message to arrive on a JMS destination. If in doubt,
     * leave it at 1.
     * @param timeout A long type. The time in seconds.
     */

    public void setTimeout(long timeout) {

        this.timeout = timeout;
    }
}
