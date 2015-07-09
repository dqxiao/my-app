package com.mycompany.reporting;

import java.io.IOException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.FanOutQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.leansoft.bigqueue.IFanOutQueue;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.util.Message;
import com.mycompany.util.NameExpr;


public class QPoller {

	private int batchSize;
	private boolean batch=false;
	private QLoggerConfig config;
	private IFanOutQueue messageBuffer=null;
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
			messageBuffer= new FanOutQueueImpl(config.getQueueDir(),config.getQueueName());
			connected=true;
		}catch(Exception e){
			System.out.printf("Not conected\n");
		}
		
	}
	
	
	private void readChunkMessage(int number, Collection<Message> result,String fanoutID) {
		try{
			for(int i=0;i<number;i++){
				byte[] b=messageBuffer.dequeue(fanoutID);
				String item=new String(b, "ISO-8859-1");
				Message mItem=new Message(item);
				result.add(mItem);
				if(messageBuffer.isEmpty(fanoutID)){
					break;
				}
			}
			// logging issue 
		}catch(Exception e){
			// logging issue 
		}
	}
	
	public Collection<Message> readStream(){
		this.start();
		String fanoutID="allKind";
		Collection<Message> result=new ArrayList<Message>();
		try{
			if(connected){
				int maxSize=(int) messageBuffer.size();
				//System.out.printf("maxSize:%d\n",maxSize);
				if(batch){
					if(maxSize>batchSize){
						readChunkMessage(batchSize, result,fanoutID);
					}
				}else{
					readChunkMessage(maxSize, result,fanoutID);
				}
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	
	private void readChunkMessageMetric(int number, Collection<Message> result,String fanoutID){
		NameExpr nameExpr=new NameExpr(fanoutID);
		int count=0;
		try{
			while(!messageBuffer.isEmpty(fanoutID)){
//				String item=new String(messageBuffer.dequeue(fanoutID));
				//String item=messageBuffer.dequeue(fanoutID).toString();
				byte[] b=messageBuffer.dequeue(fanoutID);
				String item=new String(b, "ISO-8859-1");
				Message mItem=new Message(item);
				if(nameExpr.acceptName(mItem.getAttr("name"))){
					count+=1;
					result.add(mItem);
				}
			}
		}catch(IOException e){
			
		}
		
	}
	
	
	
	/**
	 * fetch all message matching name pattern now 
	 * @param namePattern
	 * @return
	 */
	public Collection<Message> readMetric(String namePattern){
		this.start();
		Collection<Message> result=new ArrayList<Message>();
		readChunkMessageMetric(batchSize,result,namePattern);
		return result;
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
