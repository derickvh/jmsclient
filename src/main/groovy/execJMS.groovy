#!/usr/bin/env groovy
package com.oldmutual.iop

import com.oldmutual.iop.JMSConfiguration
import com.oldmutual.iop.JMSClient

File inFile = new File('jmsconfig.properties')
Properties configs = readPropsFile(inFile)

configs.each { key, value -> println("$key, $value") }

JMSConfiguration jmsConfig = new JMSConfiguration()

jmsConfig.hostname = configs.get('hostname')
jmsConfig.port = Integer.valueOf(configs.get('port'))
jmsConfig.qmanager = configs.get('qmanager')
jmsConfig.channel = configs.get('channel')
jmsConfig.destination = configs.get('destination')
jmsConfig.userID = configs.get('userID')
jmsConfig.passwd = configs.get('passwd')

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
