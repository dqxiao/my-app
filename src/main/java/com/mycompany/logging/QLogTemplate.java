package com.mycompany.logging;
import org.javasimon.callback.logging.LogTemplate;


public class QLogTemplate<C> extends  LogTemplate<C> {

	private final QLogger logger;
	
	public QLogTemplate() {
		this(new QLoggerConfig());
	}
	
	private QLogTemplate(QLoggerConfig config){
		this.logger=new QLoggerImpl(config);
	}
	
	
	public QLogTemplate(QLogger logger){
		this.logger=logger;
	} 
	
	
	
	
	
	@Override
	protected boolean isEnabled(C arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void log(String message) {
		logger.send(message);
	}

	public QLogger getQLogger(){
		return logger;
	}
	
	
	
	
}
