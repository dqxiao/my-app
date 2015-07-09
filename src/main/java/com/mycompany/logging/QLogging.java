package com.mycompany.logging;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Manager;
import org.javasimon.Meter;
import org.javasimon.Simon;
import org.javasimon.SimonFilter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;
import org.javasimon.utils.SimonUtils;

import com.mycompany.util.MeterUtil;
import com.mycompany.util.ScheduleService;
import com.mycompany.util.SimonType;
import com.mycompany.util.XMLConfigurationReader;

public class QLogging extends ScheduleService {
	
	
	private final Manager manager;
	private Collection<QLoggerFilter> configuration;
	private Map<String, LogTemplate<Meter>> meterLogTemplates=new HashMap<String, LogTemplate<Meter>>();
	private Map<String,LogTemplate<Counter>> counterLogTemplates=new HashMap<String, LogTemplate<Counter>>();
	private Map<String,LogTemplate<Split>>  stopWatchLogTemplates=new HashMap<String, LogTemplate<Split>>();
	//private 

	public QLogging(Manager manager, TimeUnit durationUnit, Collection<QLoggerFilter> loggerFilters) {
		super("QLogging",durationUnit);
		this.manager=manager;
		this.configuration=loggerFilters;
		register();
	}
	
	public static QLoggerBuilder forSimonManager(Manager manager){
		return new QLoggerBuilder(manager);
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

	
	private final LogMessageSource<Split>  stopwatchLogMessageSource = new LogMessageSource<Split>() {
		public String getLogMessage(Split split){
			String TYPE="StopWatch";
			String basicInfo=String.format("timestamp=%s,type=%s,name=%s", 
					SimonUtils.presentNanoTime(System.currentTimeMillis()),
					TYPE,
					split.getStopwatch().getName());
			
			String contentInfo=String.format("duration=%s,startTimestamp=%s",
					SimonUtils.presentNanoTime(split.runningFor()),
					SimonUtils.presentTimestamp(split.getStart())
					);
			
			return basicInfo+contentInfo;
		}
	};
	
	private final LogMessageSource<Counter>  counterLogMessageSource = new LogMessageSource<Counter>() {
		public String getLogMessage(Counter counter){
			String TYPE="Counter";
			String basicInfo=String.format("timestamp=%s,type=%s,name=%s", 
					SimonUtils.presentTimestamp(System.currentTimeMillis()),
					TYPE,
					counter.getName());
			
			String contentInfo=String.format("count=%s,max=%s,min=%s,maxTimeStamp=%s,minTimeStamp=%s",
					SimonUtils.presentMinMaxCount(counter.getCounter()),
					SimonUtils.presentMinMaxCount(counter.getMax()),
					SimonUtils.presentMinMaxCount(counter.getMin()),
					SimonUtils.presentTimestamp(counter.getMaxTimestamp()),
					SimonUtils.presentTimestamp(counter.getMinTimestamp())
					);
			
			return basicInfo+contentInfo;
		}
	};


	private final LogMessageSource<Meter> meterLogMessageSource = new LogMessageSource<Meter>() {
		public String getLogMessage(Meter meter){
			String TYPE="Meter";
			String basicInfo=String.format("timestamp=%s,type=%s,name=%s", 
					SimonUtils.presentTimestamp(System.currentTimeMillis()),
							TYPE,
							meter.getName());
			
			String contentInfo=String.format("rate=%s,PeakRate=%s,MeanRate=%s",
					MeterUtil.presentRate(meter.getOneMinuteRate(),TimeUnit.SECONDS),
					MeterUtil.presentRate(meter.getPeakRate(),TimeUnit.SECONDS),
					MeterUtil.presentRate(meter.getMeanRate(),TimeUnit.SECONDS)
					);
			
			return basicInfo+contentInfo;
		}
	};

	
	
	public  static class QLoggerBuilder
    {
		    private final Manager manager;
		    private TimeUnit durationUnit;
		    private Collection<QLoggerFilter> loggerFilters;
		    //private QLogger qLogger;
		    
		    
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
			if(filter.accept(stopwatch)){
				result.add(stopWatchLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}

	private Collection<LogTemplate<Meter>> getMeterLogTemplates(Meter meter){
		Collection<LogTemplate<Meter>> result=new ArrayList<LogTemplate<Meter>>();
		for(QLoggerFilter filter: configuration){
			if(filter.accept(meter)){
				result.add(meterLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}
	
	private Collection<LogTemplate<Counter>> getCounterLogTemplates(Counter counter){
		Collection<LogTemplate<Counter>> result=new ArrayList<LogTemplate<Counter>>();
		for(QLoggerFilter filter: configuration){
			if(filter.accept(counter)){
				result.add(counterLogTemplates.get(filter.getName()));
			}
		}
		return result;
	}
	
	
	private void reportCounter(Counter counter){
		for(LogTemplate<Counter> logTemplate: getCounterLogTemplates(counter)){
			logTemplate.log(counter, counterLogMessageSource);
		}
	}
	
	private void reportMeter(Meter meter) {
		for(@SuppressWarnings("unused") LogTemplate<Meter> logTemplate: getMeterLogTemplates(meter)){
			logTemplate.log(meter, meterLogMessageSource);
		}
	}
	
	private void reportStopWatch(Split split){
		for(LogTemplate<Split> logTemplate: getStopwatchLogTemplates(split.getStopwatch())){
			logTemplate.log(split, stopwatchLogMessageSource);
		}
	}

	
	/**
	 * simon of different type 
	 * @param simon
	 */
	private void reportSimon(Simon simon){
		//System.out.printf("simonType:%s \n", simon.getClass().getSimpleName());
		
		switch (SimonType.getSimonType(simon)) {
		case COUNTER :
			reportCounter((Counter) simon);
			break;
		case METER:
			reportMeter((Meter) simon);
			break;

		default:
			break;
		}
	}
	




	@Override
	public void reportMessage() {
	
		for(Simon simon: manager.getSimons(SimonFilter.ACCEPT_ALL_FILTER)){
			reportSimon(simon);
		}
		
		System.out.println("--------");
	}
	
	
	
	

	

}
