package com.mycompany.reporting;

import java.io.Closeable;
//import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class QScheduleReporter implements Closeable, Reporter{

	private final ScheduledExecutorService executor;
    private final double durationFactor;
    private final String durationUnit;
    private static final AtomicInteger FACTORY_ID = new AtomicInteger();
	
    
    
    /**
     * A simple named thread factory.
     */
    @SuppressWarnings("NullableProblems")
    private static class NamedThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        private NamedThreadFactory(String name) {
            final SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "metrics-" + name + "-thread-";
        }

        public Thread newThread(Runnable r) {
            final Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    
    
    
    
    /**
     * 
     * @param name
     * @param durationUnit
     */
    protected QScheduleReporter(String name,
            TimeUnit durationUnit){
    	this(durationUnit, Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(name + '-' + FACTORY_ID.incrementAndGet())));
    }
    
    protected QScheduleReporter(TimeUnit durationUnit,
            ScheduledExecutorService executor){
    	this.durationUnit=durationUnit.toString().toLowerCase(Locale.US);
    	this.durationFactor = 1.0 / durationUnit.toNanos(1);
    	this.executor=executor;
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
    
    
  
    
    public void report() {
    	//System.out.printf("call this report :%s \n", "QScheduleReporter");
    	synchronized (this) {
    		reportMessage();
    	}
	}
	
    public abstract void reportMessage();

	public void close() {
        stop();
    }
    
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


    
	
}
