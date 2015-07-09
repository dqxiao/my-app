package com.mycompany.app;

import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Meter;
import org.javasimon.MeterSample;
import org.javasimon.SimonManager;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LoggingCallback;

import com.mycompany.logging.QLoggerConfig;

//import com.mycompany.logging.MeterUtil;

import com.mycompany.logging.QLoggerFilter;
import com.mycompany.logging.QLogging;
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.management.LogReportManager;
import com.mycompany.reporting.QConsoleReporter;
import com.mycompany.util.XMLConfigurationReader;

public class App 
{
//	static void startreport(){
//		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
//				.convertDurationsTo(TimeUnit.MILLISECONDS)
//				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
//				.build();
//		
//		reporter.start(1, TimeUnit.SECONDS);
//	
//	}
//	
//	static void startLogging(){
//		QLogging logging=QLogging.forSimonManager(SimonManager.manager())
//				.convertDurationsTo(TimeUnit.MILLISECONDS)
//				.readConfig("./config/loggerConfig.xml")
//				.build();
//		
//		logging.start(2, TimeUnit.SECONDS);
//	}
	
    public static void main( String[] args )
    {
      
//    	String DEFAULT_CONFIG_FILE="./config/loggerConfig.xml";
//    	
//    	for(QLoggerFilter filter: new XMLConfigurationReader(DEFAULT_CONFIG_FILE).getLoggerFilter()){
//    		System.out.println(filter.toString());
//    	}
//    	//System.out.printf("HelloWorld %s \n", "Code");
//    	//startreport();
////    	startLogging();
    	
    	LogReportManager manager=new LogReportManager();
    	manager.start();
    	Counter counter=SimonManager.getCounter("HellWorld-Counter");
    	Meter meter=SimonManager.getMeter("HelloWorld-Meter");
    	
    
    	
    	for(int i=0;i<11;i++){
    		counter.increase();
    		meter.mark(i*1000);
    		
    		
    		
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    }
}
