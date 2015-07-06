package com.mycompany.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public abstract class ScheduleService implements Closeable  {

	private String durationUnit;
	private double durationFactor;
	private ScheduledExecutorService executor;
	private static final AtomicInteger FACTORY_ID = new AtomicInteger();


	public void close() throws IOException {
		stop();
	}
	
	
	  public void start(long period, TimeUnit unit) {
	        executor.scheduleAtFixedRate(new Runnable() {
	            public void run() {
	                try {
	                    report();
	                } catch (RuntimeException ex) {
	                   //
	                }
	            }
	        }, period, period, unit);
	    }
	
	
	  protected ScheduleService(String name,
	            TimeUnit durationUnit){
	    	this(durationUnit, Executors.newSingleThreadScheduledExecutor(
	    			  new NamedThreadFactory(name + '-' +
	    					  FACTORY_ID.incrementAndGet()
	    			  )
	    			)
	    		);
	    }
	
	  
	  public void report() {
	    	synchronized (this) {
	    		reportMessage();
	    	}
		}
	
   
	  
	public abstract void reportMessage();
    
    
    private void stop(){
    	executor.shutdown();
    	try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow(); 
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    System.err.println(getClass().getSimpleName() + ": ScheduledExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ie) {
         
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    	
    }


	protected ScheduleService(TimeUnit durationUnit,
            ScheduledExecutorService executor){
    	this.durationUnit=durationUnit.toString().toLowerCase(Locale.US);
    	this.durationFactor = 1.0 / durationUnit.toNanos(1);
    	this.executor=executor;
    }
    
    
    

}
