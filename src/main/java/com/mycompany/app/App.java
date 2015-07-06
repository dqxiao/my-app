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
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.logging.XMLConfigurationReader;
import com.mycompany.reporting.QConsoleReporter;

public class App 
{
	static void startreport(){
		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
				.build();
		
		reporter.start(1, TimeUnit.SECONDS);
	
	}
	
    public static void main( String[] args )
    {
      
    	String DEFAULT_CONFIG_FILE="./config/loggerConfig.xml";
    	
//    	for(QLoggerFilter filter: new XMLConfigurationReader(DEFAULT_CONFIG_FILE).getLoggerFilter()){
//    		System.out.println(filter.toString());
//    	}
    	//System.out.printf("HelloWorld %s \n", "Code");
    	//startreport();
    	Counter counter=SimonManager.getCounter("HellWorld-Counter");
    	Meter meter=SimonManager.getMeter("HelloWorld-Meter");
    	
		SimonManager.callback().addCallback(
				new QLoggingCallback()
//			new CallbackSkeleton(){
//				public void onMeterIncrease(Meter meter, long inc, MeterSample sample){
//					System.out.printf("meter-increase:%s \n", meter.toString());
//				}
//				public void onCounterIncrease(Counter counter, long inc, CounterSample sample){
//					System.out.printf("Counter-Increase\n");
//				}
//			}
		);
    	
    	//System.out.printf("Second%d\n",TimeUnit.SECONDS.toNanos(5L));
    	
		
		
    
    	
    	for(int i=0;i<11;i++){
    		//counter.increase();
    		meter.mark(i*1000);
    		
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    }
}
