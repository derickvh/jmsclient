#!/usr/bin/env groovy
package com.oldmutual.iop

import static com.oldmutual.iop.JMSClient.write

/**
 * Created by derick on 2016/09/01.
 */
import static java.lang.Integer.valueOf

File inFile = new File('jmsconfig.properties')
Properties configs = readPropsFile(inFile)

JMSConfiguration jmsConfig = new JMSConfiguration()

jmsConfig.hostname = configs['hostname']
jmsConfig.port = valueOf((String) configs['port'])
jmsConfig.qmanager = configs['qmanager']
jmsConfig.channel = configs['channel']
jmsConfig.destination = configs['destination']
jmsConfig.userID = configs['userID']
jmsConfig.passwd = configs['passwd']

def msg = "The quick brown fox is 'n vervlakste skelm en lui jakkals."

        write(jmsConfig, msg)

println 'Send message: [' + msg + ']'

static Properties readPropsFile(File fProps) {

    if (!fProps.exists()) {
        throw new Exception(
                "Required properties file does not exist: $fProps.canonicalPath")
    }
    Properties props = new Properties()
    fProps.withInputStream { stream -> props.load(stream) }
    return props
}
/**
 * Created by derick on 2016/09/01.
 */
