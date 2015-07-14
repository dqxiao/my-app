package com.mycompany.logging;



/**
 * QLoggerConfig. Configuration about message buffer 
 * QueueDir 
 * QueueName
 * @author xiaod
 *
 */
public class QLoggerConfig {
	
	private String queueName;
	private String queueDir;
	
	private final static String  DEFAULT_DIR="./Metric/report";
	private final static String  PHONE_HOME_QUEUENAME="PhoneHome";
	
	public QLoggerConfig() {
		this.queueDir="./Metric/report";
		this.queueName="Default";
	}
	
	
	public QLoggerConfig(String queueDir,String queueName) {
		this.queueDir=queueDir;
		this.queueName=queueName;
	}
	
	public QLoggerConfig(String queueName){
		this(DEFAULT_DIR,queueName);
	}
	
	public static QLoggerConfig PhoneHomeConfig() {
		return new QLoggerConfig(DEFAULT_DIR,PHONE_HOME_QUEUENAME);
	}

	
	public String getQueueDir() {
		return queueDir;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	public void setQueueDir(String queueDir) {
		this.queueDir=queueDir;
	}
	
	public void setQueueName(String queueName) {
		this.queueName=queueName;
	}
	
	@Override public boolean equals(Object other) {
	    boolean result = false;
	    if(other instanceof QLoggerConfig){
	    	QLoggerConfig that=(QLoggerConfig) other;
	    	if(that.getQueueDir().equals(queueDir) && that.getQueueName().equals(queueName)){
	    		result=true;
	    	}
	    }
	    return result;
	}

}
