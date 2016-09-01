package com.oldmutual.iop;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JMSClientTest {

    private static JMSConfiguration jmsC = new JMSConfiguration();

    @BeforeClass
    public static void configureJMS() {

        jmsC.setChannel("SERVCONN");
        jmsC.setDestination("JMSQ1");
        jmsC.setHostname("localhost");
        jmsC.setQmanager("QMSPINOZA");
        jmsC.setPort(1415);
        jmsC.setUserID("derick");
        jmsC.setPasswd("nopasswd");
        jmsC.setTimeout(1L);
    }

    @Test
    public void testWrite() {

        String textMsg = "The quick brown fox jumps over the lazy dog.";

        assertTrue(JMSClient.write(jmsC, textMsg));

        System.out.print(textMsg);
        System.out.println(" sent successfully");

    }

    @Test
    public void testRead() {

        String returnTxt = JMSClient.read(jmsC);

        assertTrue(returnTxt != null);

        System.out.print(returnTxt);
        System.out.println(" read successfully");
    }

    @Test
    public void testWriteMany() {

        Path dir = Paths.get("/home/derick/messages");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            List<String> msgs = new ArrayList<>();

            for (Path entry : stream) {

                System.out.print("Reading file: ");
                System.out.println(entry.toString());

                msgs.add(new String(Files.readAllBytes(Paths.get(entry.toString()))));
            }
            assertTrue(JMSClient.writeMany(jmsC, msgs));
            System.out.println("Messages sent successfully");

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Test
    public void testReadMany() {

        List<String> msgs = JMSClient.readMany(jmsC);

        assertEquals(10, msgs.size());
        System.out.println("Read " + msgs.size() + " messages");
    }
}
