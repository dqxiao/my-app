package com.mycompany.management;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.javasimon.SimonManager;

import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLoggerFilter;
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.reporting.QConsoleReporter;
import com.mycompany.util.XMLConfigurationReader;

//import org.javasimon.Simon;

public class LogReportManager {
	

	//String  
	
	public LogReportManager(){
		
	}

	
	public void start(){
		//disable();
		if(SimonManager.isEnabled()){
			startLogging();
			startReporting();
		}
		
	}
	
	private void disable(){
		SimonManager.disable();
	}
	
	private void startLogging(){
		Collection<QLoggerFilter> qLoggerFilters=null;
		try{
			qLoggerFilters=new XMLConfigurationReader("./config/loggerConfig.xml").getLoggerFilter();
		}catch(Exception e){
			
		}
		
		if(qLoggerFilters!=null){
		SimonManager.callback().addCallback(	
				new QLoggingCallback(qLoggerFilters)
				);
		}
	}
	
	private void startReporting(){
		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
				.build();
		
		reporter.start(1, TimeUnit.SECONDS);
	}
}
