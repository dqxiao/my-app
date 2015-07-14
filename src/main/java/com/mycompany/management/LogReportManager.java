package com.mycompany.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.Meter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import com.mycompany.logging.QLogTemplateConfig;
import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLoggerFilter;
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.publish.QConsoleReporter;
import com.mycompany.util.XMLConfigurationReader;


/*
 * Static utility 
 * */
public final class LogReportManager {

	
	static {
		System.out.printf("init logReporter \n");
		init();
	}
	
	public LogReportManager(){
		//
	}
	
	
	public static Stopwatch getStopwatch(String stopWatchName){
		return SimonManager.getStopwatch(stopWatchName);
	}
	
	public static Counter getCounter(String counterName){
		return SimonManager.getCounter(counterName);
	}
	
	public static Meter getMeter(String meterName){
		return SimonManager.getMeter(meterName);
	}
	
	


	
	public static void init(){
		if(SimonManager.isEnabled()){
			startLogging();
			startReporting();
		}
	}
	
	private void disable(){
		SimonManager.disable();
	}
	
	private static void startLogging(){
		Collection<QLoggerFilter> qLoggerFilters=new ArrayList<QLoggerFilter>();
		Collection<QLogTemplateConfig> configs=new ArrayList<QLogTemplateConfig>();
		try{
			XMLConfigurationReader reader=new XMLConfigurationReader("./config/loggerConfig.xml");
			qLoggerFilters=reader.getLoggerFilter();
			configs=reader.getQLogTemplateConfig();
		}catch(Exception e){
			
		}
		
		
		
		if(qLoggerFilters!=null){
		SimonManager.callback().addCallback(	
				new QLoggingCallback(qLoggerFilters,configs)
				);
		}
	}
	
	private static void startReporting(){
		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
				.setBatchSize(10)
				.build();
		
		reporter.start(2, TimeUnit.SECONDS);
	}
}
