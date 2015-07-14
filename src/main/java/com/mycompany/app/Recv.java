package com.mycompany.app;

import java.io.IOException;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.util.Message;

public class Recv {

	 public static void main( String[] args ) throws IOException{
		 
		 QLoggerConfig pConfig= new QLoggerConfig("./Metric/report", "PhoneHome");
//		 IBigQueue messageBuffer= new BigQueueImpl(pConfig.getQueueDir(),pConfig.getQueueName());
//		 
//		 int maxSize=(int) messageBuffer.size();
//		 System.out.printf("size:%d\n",maxSize);
//		 for(int i=0;i<maxSize;i++){
//			 System.out.printf("Item:%s\n",new Message(new String(messageBuffer.dequeue())).getContent());
//		 }
		 
		 
	 }
}
