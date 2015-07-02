package com.mycompany.reporting;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.mycompany.logging.QLoggerConfig;


public class QPoller {

	private int batchSize;
	private boolean batch=false;
	private QLoggerConfig config;
	private IBigQueue messageBuffer=null;
	private boolean connected=false;
	
	
	public QPoller(){
		this(new QLoggerConfig(), 1);
	}
	
	public QPoller(QLoggerConfig config,int batchSize){
		this.config=config;
		this.batchSize=batchSize;
		if(batchSize!=1){
			batch=true;
		}
	}
	
	/**
	 * Connect to local message buffer
	 */
	public void start(){
		try{
			messageBuffer= new BigQueueImpl(config.getQueueDir(),config.getQueueName());
			//System.out.printf("connect to local messageBuffer:%s\n",config.get);
			connected=true;
		}catch(Exception e){
			System.out.printf("Not conected\n");
		}
		
	}
	
	private void readChunkMessage(int number, Collection<String> messages) {
		try{
			for(int i=0;i<number;i++){
				messages.add(new String(messageBuffer.dequeue()));
			}
			// logging issue 
		}catch(Exception e){
			// logging issue 
		}
	}
	
	public Collection<String> read(){
		Collection<String> messages=new ArrayList<String>();
		this.start();
		
		if(connected){
			int maxSize=(int) messageBuffer.size();
			if(batch){
				if(maxSize>batchSize){
					readChunkMessage(batchSize, messages);
				}
			}else{
				readChunkMessage(maxSize, messages);
			}
		}
		
		return messages;
	}
	
	public void setBatchSize(int batchSize){
		this.batchSize=batchSize;
		if(this.batchSize!=1){
			batch=true;
		}
	}
	
	public void setConfig(QLoggerConfig config){
		this.config=config;
	}
}
