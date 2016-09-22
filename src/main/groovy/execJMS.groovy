#!/usr/bin/env groovy

import static java.lang.Integer.valueOf

File inFile = new File('jmsconfig.properties')
Properties configs = readPropsFile(inFile)

//configs.each { key, value -> println("$key, $value") }

JMSConfiguration jmsConfig = new JMSConfiguration()

jmsConfig.hostname = configs['hostname']
jmsConfig.port = valueOf((String) configs['port'])
jmsConfig.qmanager = configs['qmanager']
jmsConfig.channel = configs['channel']
jmsConfig.destination = configs['destination']
jmsConfig.userID = configs['userID']
jmsConfig.passwd = configs['passwd']

String msg = 'The quick brown fox jumps over the lazy dog.'

JMSClient.write(jmsConfig, msg)

println("Message sent: $msg")

String rmsg = JMSClient.read(jmsConfig)

println("Message received: $rmsg")

Properties readPropsFile(File fProps) {

 if (!fProps.exists()) {
     throw new Exception(
     "Required properties file does not exist: $fProps.canonicalPath")
 }
  Properties props = new Properties()
  fProps.withInputStream { stream -> props.load(stream) }
  return props
}
