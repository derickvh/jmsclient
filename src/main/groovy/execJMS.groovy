#!/usr/bin/env groovy

//import com.oldmutual.iop.JMSConfiguration
//import com.oldmutual.iop.JMSClient

File inFile = new File('jmsconfig.properties')
Properties configs = readPropsFile(inFile)

configs.each { key, value -> println("$key, $value") }
/*
JMSConfiguration jmsConfig = new JMSConfiguration()

jmsConfig.hostname = configs.get('hostname')
jmsConfig.port = configs.get('port')
jmsConfig.qmanager = configs.get('qmanager')
jmsConfig.channel = configs.get('channel')
jmsConfig.destination = configs.get('destination')
jmsConfig.userID = configs.get('userID')
jmsConfig.password = configs.get('password')

String msg = 'The vinnige bruin jakals is mal en hardloop heen en weer sonder rigting waarna hy oor die lui hond spring.'

JMSClient jmsC = new JMSClient()

assertTrue jmsC.write(jmsConfig, msg)

println('Message sent: $msg')
println

assertTrue msg == jmsC.read(jmsConfig)

println('Message received: $msg')
println
*/
Properties readPropsFile(File fProps) {

 if (!fProps.exists()) {
     throw new Exception(
     "Required properties file does not exist: $fProps.canonicalPath")
 }
  Properties props = new Properties()
  fProps.withInputStream { stream -> props.load(stream) }
  return props
}
