package com.mycompany.logging;
import org.javasimon.callback.logging.LogTemplate;


/**
 * A logging template allows us to customize the logging behavior of different metric collector 
 * @author xiaod
 *
 * @param <C>
 */
public  class QLogTemplate<C> extends  LogTemplate<C> {

	private QLogger logger;
	
	
	public QLogTemplate(){
		this.logger=null;
	}
	
	public QLogTemplate(QLogger logger){
		this.logger=logger;
	} 
	
	public QLogger getQLogger(){
		return this.logger;
	}
	
	public void setQLogger(QLogger logger){
		this.logger=logger;
	}
	
	//public QLogTemplate<C> 
	
	@Override
	protected boolean isEnabled(C context) {
		return true;
	}

	@Override
	protected void log(String message) {
		//System.out.print("try to send\n");
		if(logger!=null){
			logger.send(message);
		}else{
			System.out.printf("ERROR:", "logging using null logger \n");
		}
	}


}
