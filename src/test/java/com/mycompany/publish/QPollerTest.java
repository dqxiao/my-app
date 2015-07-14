package com.mycompany.publish;

import static org.junit.Assert.*;


import java.util.Collection;

import org.junit.Test;

import com.mycompany.logging.QLogger;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLoggerImpl;
import com.mycompany.util.Message;

public class QPollerTest {

	@Test
	public void testConsturtor(){
		
		QPoller poller=new QPoller();
		QLoggerConfig config=new QLoggerConfig();
		assertEquals(config,poller.getConfig());
		assertEquals(poller.getBatchSize(), 1);
		//
		int batchSize=2;
		poller.setBatchSize(batchSize);
		assertEquals(poller.getBatchSize(), batchSize);
		
		QLoggerConfig phoneConfig=QLoggerConfig.PhoneHomeConfig();
		poller.setConfig(phoneConfig);
		assertEquals(poller.getConfig(), phoneConfig);
	}
	
	
//	@Test
//	public void testPolling(){
//		// put message into message buffer 
//		QLoggerConfig config=new QLoggerConfig();
//		QLogger logger=new QLoggerImpl(config);
//		
//		Message message=new Message();
//			message.withInfo("test","a");
//			message.withInfo("name", "helloWorld");
//		logger.start();
//		logger.send(message.toString());
//		
//		// get message
//		QPoller poller =new QPoller(config, 1);
//		
//		Collection<Message> result=poller.pollStream();
//		//assertEquals(result.size(), 1);
//		assertEquals(1, result.size());
//	}
	
	

}
