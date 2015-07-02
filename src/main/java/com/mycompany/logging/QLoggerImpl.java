package com.mycompany.logging;



import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLogger;


public class QLoggerImpl implements QLogger {
	
	private IBigQueue bigQueue=null;
	private QLoggerConfig loggerConfig;
	
	private enum EVENT{
		START,CONNECT,SEND,CLOSE
	} 
	
	private enum STATUS{
		SUCCED,FAIL
	}


	public QLoggerImpl() {
		this(new QLoggerConfig());
	}

	
	public QLoggerImpl(QLoggerConfig config){
		this.loggerConfig=config;
		this.start();
	}

	

	public void start(){
		try{
			bigQueue = new BigQueueImpl(loggerConfig.getQueueDir(),loggerConfig.getQueueName());
		}catch(Exception e){
			//System.out.println("can't start the service");
			System.out.println(STATUS.FAIL+"to"+EVENT.START);
		}
	}

	public void send(String message){
		if(bigQueue!=null){
			try{
				bigQueue.enqueue(message.getBytes());
			}catch(Exception e){
				System.out.println(STATUS.FAIL+"to"+EVENT.SEND);
			}
		}

	}

	public void  close(){
		if(bigQueue!=null){
			try {
				bigQueue.close();
			}catch(Exception e) {
				
			}
		}
	}

	/*
	 * waiting for implementation*/
	public void info(){

	}

	/*
	 * waiting for implementation*/
	public void setConfig(QLoggerConfig config) {
	
		
	}

}
