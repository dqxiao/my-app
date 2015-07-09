package com.mycompany.app;

import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
//import org.javasimon.Meter;
import org.javasimon.SimonManager;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LoggingCallback;

import com.mycompany.logging.QLoggerConfig;
import com.mycompany.logging.QLoggingCallback;
import com.mycompany.reporting.QConsoleReporter;

public class HellWorld {
	
	public static void main( String[] args ) throws InterruptedException{
		
//		SimonManager.callback().addCallback(
//				new CallbackSkeleton(){
//					@Override
//						public void onCounterIncrease(Counter counter, long inc, CounterSample sample) {
//						System.out.printf("counter\n");
//					}
//				}
//		);
		
		startreport();
		//SimonManager.callback().addCallback(new QLoggingCallback());
//		Meter meter=SimonManager.getMeter("HelloWorld-Meter");
		Counter counter=SimonManager.getCounter("HelloWorld-Counter");
		for(int i=0;i<10;i++){
			Thread.sleep(1000);
//			counter.increase(i);
//			meter.mark(i);
			System.out.printf("i:%d \n",i);
		}
		
	}
	
	
	static void startreport(){
		QConsoleReporter reporter=QConsoleReporter.forSimMonager()
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.setConfig(new QLoggerConfig("./Metric/report", "PhoneHome"))
				.setBatchSize(2)
				.build();
		
		reporter.start(1, TimeUnit.SECONDS);
	
	}

}
