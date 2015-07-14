package com.mycompany.publish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.leansoft.bigqueue.FanOutQueueImpl;
import com.leansoft.bigqueue.IFanOutQueue;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.util.AggregatedOverhead;
import com.mycompany.util.Message;
import com.mycompany.util.MonitorUtil;
import com.mycompany.util.NameExpr;
/**
 *
 * 
 *
 * @author xiaod
 *
 */
public final class QPoller implements MetricPoller {

	private int batchSize;
	private QLoggerConfig config;
	private final String WILD_START="*";
	private final String ENCODEC="ISO-8859-1";
	private AggregatedOverhead pushLatency=new AggregatedOverhead();
	
	public QPoller(){
		this(new QLoggerConfig(), 1);
	}
	
	public QPoller(QLoggerConfig config,int batchSize){
		this.config=config;
		this.batchSize=batchSize;
	}
	
	
	

	
	public void setBatchSize(int batchSize){
		this.batchSize=batchSize;
	}
	
	public void setConfig(QLoggerConfig config){
		this.config=config;
	}
	
	public int getBatchSize(){
		return this.batchSize;
	}
	
	public QLoggerConfig getConfig(){
		return this.config;
	}

	
	@Override
	public Collection<Message> poll(String namePattern) {
		String fanoutID=namePattern;
		Collection<Message> result=new ArrayList<Message>();
		// build the connection to local 
		try{
			IFanOutQueue messageBuffer= new FanOutQueueImpl(config.getQueueDir(),config.getQueueName());
			int mSize=(int) messageBuffer.size();
			
			
			if(mSize>=batchSize){
				pollMetrics(batchSize, result, messageBuffer, fanoutID);
			}
			messageBuffer.close();
		}catch(Exception e){
			// logger issue 
		}
		
		
		return result;
	}
	
	

	@Override
	public Collection<Message> pollStream() {
		return poll("*");
	}
	

	private void pollMetrics(int number,Collection<Message> result,IFanOutQueue messageBuffer,String fanoutID){
		NameExpr nameExpr=new NameExpr(fanoutID);
		int count=0;
		try{
			while(!messageBuffer.isEmpty(fanoutID) && count<number){
				long inTimeStamp=messageBuffer.peekTimestamp(fanoutID);
				byte[] b=messageBuffer.dequeue(fanoutID);
				
				String item=new String(b, ENCODEC);
				Message mItem=new Message(item);
				
				pushLatency.observeDiff(
						MonitorUtil.parseTimeStamp(mItem.getAttr("timestamp")), inTimeStamp);
				
				
				if(nameExpr.acceptName(mItem.getAttr("name"))){
					result.add(mItem);
					count+=1;
				}
			}
		}catch(IOException e){
			
		}
		//System.out.printf("poll %d\n",count);
		
	}
	
	
	public String statis(){
		return pushLatency.statis();
	}
	
}
