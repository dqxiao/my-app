package com.mycompany.app;

import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.SimonManager;
import org.javasimon.callback.CallbackSkeleton;

import com.mycompany.logging.QLoggerConfig;

//import com.mycompany.logging.MeterUtil;

import com.mycompany.logging.QLoggerFilter;
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.logging.XMLConfigurationReader;
import com.mycompany.reporting.QConsoleReporter;

public class App 
{
	static void startreport(){
		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
				.setBatchSize(2)
				.build();
		
		reporter.start(1, TimeUnit.SECONDS);
	
	}
	
    public static void main( String[] args )
    {
      
    	String DEFAULT_CONFIG_FILE="./config/loggerConfig.xml";
    	
    	for(QLoggerFilter filter: new XMLConfigurationReader(DEFAULT_CONFIG_FILE).getLoggerFilter()){
    		System.out.println(filter.toString());
    	}
    	System.out.printf("HelloWorld %s \n", "Code");
    	startreport();
    	Counter counter=SimonManager.getCounter("HellWorld");
		SimonManager.callback().addCallback(
				new QLoggingCallback()
		);
    	
    	
    	for(int i=0;i<10;i++){
    		counter.increase();
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
}
