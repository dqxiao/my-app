package com.mycompany.reporting;

import java.io.PrintStream;
//import java.nio.file.attribute.AclEntry.Builder;
import java.util.Locale;
//import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mycompany.logging.QLoggerConfig;

public class QConsoleReporter extends QScheduleReporter {
	

	private final PrintStream output;
	private final QPoller qPoller;
	
	public static Builder  forSimMonager(){
		return new Builder();
	}
	
	
	private QConsoleReporter(PrintStream output,TimeUnit durationUnit,QPoller qPoller){
		super("console-reporter", durationUnit);
		this.output=output;
		this.qPoller=qPoller;
	}
	
	 public static class Builder {
		 private PrintStream output;
	     private Locale locale;
	     private TimeUnit durationUnit;
	     private QPoller qPoller;
	     
		 private Builder(){
			 this.output=System.out;
			 this.locale=Locale.getDefault();
			 this.durationUnit=TimeUnit.MILLISECONDS;
			 this.qPoller=new QPoller();
		 }
		 
		 public Builder outputTo(PrintStream output){
			 this.output=output;
			 return this;
		 }
		 
		 public Builder formattedFor(Locale locale){
			 this.locale=locale;
			 return this;
		 }
		 
		 
		  public Builder convertDurationsTo(TimeUnit durationUnit) {
	            this.durationUnit = durationUnit;
	            return this;
	        }
		  public Builder setBatchSize(int batchSize){
			  this.qPoller.setBatchSize(batchSize);
			  return this;
		  }
		  public Builder setConfig(QLoggerConfig config){
			  this.qPoller.setConfig(config);
			  return this;
		  }
		  
		  public QConsoleReporter build(){
			
			  return new QConsoleReporter(
					  output, 
					  durationUnit, 
					  qPoller);
		  }
	 }
	

	@Override
	public void reportMessage() {
		// TODO Auto-generated method stub
		System.out.println("QConsole report\n");
		 for(String message: qPoller.read()){
			 System.out.println(message);
		 }
		 System.out.println("*******");
		
	}

}
