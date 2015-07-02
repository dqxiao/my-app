package com.mycompany.app;

import java.io.IOException;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
//import com.mycompany.logging.QLoggerConfig;

public class HelloWorldMonitoring {
	public static void main( String[] args ) throws IOException{
		
		
		IBigQueue bigQueue=new BigQueueImpl("./Metric/report","PhoneHome");
		
//		for(int i=0;i<bigQueue.size();i++){
//			System.out.println(new String(bigQueue.dequeue()));
//		}
		//System.out.printf("bigQueueSize:%s\n", bigQueue.size());
		int maxSize=(int) bigQueue.size();
		for(int i=0;i<maxSize;i++){
			System.out.println(new String(bigQueue.dequeue()));
		}
		bigQueue.close();
	}
}
