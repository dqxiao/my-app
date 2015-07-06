package com.mycompany.logging;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.Manager;
import org.javasimon.Meter;
import org.javasimon.Simon;
import org.javasimon.SimonFilter;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.logging.LogTemplate;

import com.mycompany.util.ScheduleService;

public class QLogging extends ScheduleService {
	
	
	private final Manager manager;
	private Collection<QLoggerFilter> configuration;
	private Map<String, LogTemplate<Meter>> meterLogTemplates=new HashMap<String, LogTemplate<Meter>>();
	private Map<String,LogTemplate<Counter>> counterLogTemplates=new HashMap<String, LogTemplate<Counter>>();
	private Map<String,LogTemplate<Split>>  stopWatchLogTemplates=new HashMap<String, LogTemplate<Split>>();
	

	public QLogging(Manager manager, TimeUnit durationUnit, Collection<QLoggerFilter> loggerFilters) {
		super("QLogging",durationUnit);
		this.manager=manager;
		this.configuration=loggerFilters;
		
	}

	private void register(){
		for(QLoggerFilter filter: configuration){
			registerFilter(filter);
		}
	}

	
	private void registerFilter(QLoggerFilter filter){
		QLogger logger=new QLoggerImpl(new QLoggerConfig(filter.getName()));
		meterLogTemplates.put(filter.getName(), new QLogTemplate<Meter>(logger));
		counterLogTemplates.put(filter.getName(), new QLogTemplate<Counter>(logger));
		stopWatchLogTemplates.put(filter.getName(), new QLogTemplate<Split>(logger));
	}

	









	
	
	public  static class QLoggerBuilder
    {
		    private final Manager manager;
		    private TimeUnit durationUnit;
		    private Collection<QLoggerFilter> loggerFilters;
		    
		    
		    private QLoggerBuilder(Manager manager)
		    {
		      this.manager = manager;
		      this.durationUnit = TimeUnit.MILLISECONDS;
		    }
		    
		    
		    
		    public QLoggerBuilder convertDurationsTo(TimeUnit durationUnit)
		    {
		      this.durationUnit = durationUnit;
		      return this;
		    }
		    
		    public QLoggerBuilder readConfig(String configFile){
		    	loggerFilters=new XMLConfigurationReader(configFile).getLoggerFilter();
		    	return this;
		    }
		    
		    public QLogging build()
		    {
		      return new QLogging(this.manager, this.durationUnit,loggerFilters);
		    }
	    
	    
	  }
	
	
	private Collection<LogTemplate<Split>> getStopwatchLogTemplates(Stopwatch stopwatch){
		Collection<LogTemplate<Split>> result=new ArrayList<LogTemplate<Split>>();
		for(QLoggerFilter filter:configuration){
			if(filter.accepted(stopwatch)){
				result.add(stopWatchLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}

	private Collection<LogTemplate<Meter>> getMeterLogTemplates(Meter meter){
		Collection<LogTemplate<Meter>> result=new ArrayList<LogTemplate<Meter>>();
		for(QLoggerFilter filter: configuration){
			if(filter.accepted(meter)){
				result.add(meterLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}
	
	private Collection<LogTemplate<Counter>> getCounterLogTemplates(Counter counter){
		Collection<LogTemplate<Counter>> result=new ArrayList<LogTemplate<Counter>>();
		for(QLoggerFilter filter: configuration){
			if(filter.accepted(counter)){
				result.add(counterLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}
	

	
	/**
	 * 
	 * @param simon
	 */
	private void reportSimon(Simon simon){
		//
	}
	


	@Override
	public void reportMessage() {
	
		for(Simon simon: manager.getSimons(SimonFilter.ACCEPT_ALL_FILTER)){
			
		}
	}
	
	

	

}
