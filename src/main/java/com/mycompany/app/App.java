package com.mycompany.app;


import org.javasimon.Counter;
import org.javasimon.Meter;
import org.javasimon.SimonManager;

import com.mycompany.management.LogReportManager;




public class App 
{

	
    public static void main( String[] args )
    {
      

    	
//    	LogReportManager manager=new LogReportManager();
//    	manager.start();
    	/**
    	 * 
    	 */
    	Counter counter=LogReportManager.getCounter("HelloWorld-Counter");
    	Meter meter=LogReportManager.getMeter("HelloWorld-Meter");
//    	Counter  worldCounter=SimonManager.getCounter("World-Counter");
    	
    
    	
    	for(int i=0;i<1000;i++){
    		counter.increase();
    		meter.mark(i*10);
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
//    	
    	System.out.printf("worldCounter start \n");
    	
    	for(int i=0;i<1000;i++){
//    		worldCounter.increase();
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    }
}
