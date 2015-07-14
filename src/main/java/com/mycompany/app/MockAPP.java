package com.mycompany.app;


import com.mycompany.logging.QLogger;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLoggerImpl;
import com.mycompany.publish.QPoller;
import com.mycompany.util.Message;

public class MockAPP {

	public static void main(String[] args){
		QLoggerConfig config=new QLoggerConfig("./Metric/report","PhoneHome");
		QLogger logger=new QLoggerImpl(config);
		logger.start();
		
		Message message =new Message();
		message.withInfo("name", "helloWorld");
		message.withInfo("type","Counter");
		message.withInfo("count", "1");
		
		
		logger.send(message.toString());;
		
		
		// read 
		QPoller poller=new QPoller(config, 1);
		
		for(Message mitem: poller.pollStream()){
			System.out.printf("value:%s\n", mitem.getAttr("count"));
		}
	}
}
