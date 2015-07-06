package com.mycompany.logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
//import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Meter;
import org.javasimon.MeterSample;
//import org.javasimon.Meter;
//import org.javasimon.MeterSample;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;
import org.javasimon.utils.SimonUtils;

public class QLoggingCallback extends CallbackSkeleton {
	
	private Map<String, LogTemplate<Meter>> meterLogTemplates=new HashMap<String, LogTemplate<Meter>>();
	private Map<String,LogTemplate<Counter>> counterLogTemplates=new HashMap<String, LogTemplate<Counter>>();
	private Map<String,LogTemplate<Split>>  stopWatchLogTemplates=new HashMap<String, LogTemplate<Split>>();
	private Map<String, QLoggerFilter> configuration;
	private final static String DEFAULT_CONFIG_FILE="./config/loggerConfig.xml";
	
	
	public QLoggingCallback(){
		clear();
		readConfig(DEFAULT_CONFIG_FILE);
	}
	
	private void readConfig(String configFile){
		for(QLoggerFilter filter: new XMLConfigurationReader(configFile).getLoggerFilter()){
			registerByQLoggerFiler(filter);
		}
	}
	
	
	
	private void registerByQLoggerFiler(QLoggerFilter filter){
		if(!configuration.containsKey(filter.getName())){
			configuration.put(filter.getName(),filter);
			registerLogTemplate(filter);
		}else{
			//Logger issue 
		}
	}
	
	private void registerLogTemplate(QLoggerFilter filter){
		QLogger logger=new QLoggerImpl(new QLoggerConfig(filter.getName()));
		
		meterLogTemplates.put(filter.getName(), new QLogTemplate<Meter>(logger));
		counterLogTemplates.put(filter.getName(), new QLogTemplate<Counter>(logger));
		stopWatchLogTemplates.put(filter.getName(), new QLogTemplate<Split>(logger));
		//meterLogTemplates.put(filter.getName(), new QLogTemplate<Meter>(new QLoggerConfig(filter.getName())));
		//counterLogTemplates.put(filter.getName(), new QLogTemplate<Counter>(new QLoggerConfig(filter.getName())));
		//stopWatchLogTemplates.put(filter.getName(), new QLogTemplate<Split>(new QLoggerConfig(filter.getName())));
	}
	
	
	private void clear(){
		configuration=new LinkedHashMap<String, QLoggerFilter>();
	}
	
	
	
	
	
	/**Split to string converter*/
	private final LogMessageSource<Split>  stopwatchLogMessageSource = new LogMessageSource<Split>() {
		public String getLogMessage(Split split){
			String TYPE="StopWatch";
			String basicInfo=String.format("type=%s,name=%s", 
					TYPE,split.getStopwatch().getName());
			
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
			String basicInfo=String.format("type=%s,name=%s", 
					TYPE,counter.getName());
			
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
			String basicInfo=String.format("type=%s,name=%s", 
					TYPE,meter.getName());
			
			String contentInfo=String.format("rate=%s,PeakRate=%s,MeanRate=%s",
					MeterUtil.presentRate(meter.getOneMinuteRate(),TimeUnit.SECONDS),
					MeterUtil.presentRate(meter.getPeakRate(),TimeUnit.SECONDS),
					MeterUtil.presentRate(meter.getMeanRate(),TimeUnit.SECONDS)
					);
			
			return basicInfo+contentInfo;
		}
	};
	
	
	private Collection<LogTemplate<Split>> getStopwatchLogTemplates(Stopwatch stopwatch){
		//System.out.printf("Find out accepted\n");
		Collection<LogTemplate<Split>> result=new ArrayList<LogTemplate<Split>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accepted(stopwatch)){
				//System.out.printf("Accpted\n");
				result.add(stopWatchLogTemplates.get(name));
			}
		}
		return result;
	}

	private Collection<LogTemplate<Meter>> getMeterLogTemplates(Meter meter){
		Collection<LogTemplate<Meter>> result=new ArrayList<LogTemplate<Meter>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accepted(meter)){
				result.add(meterLogTemplates.get(name));
			}
		}
		return result;
	}
	
	private Collection<LogTemplate<Counter>> getCounterLogTemplates(Counter counter){
		Collection<LogTemplate<Counter>> result=new ArrayList<LogTemplate<Counter>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accepted(counter)){
				result.add(counterLogTemplates.get(name));
			}
		}
		return result;
	}
	
	
	
	
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		for(LogTemplate<Split> logTemplate: getStopwatchLogTemplates(split.getStopwatch())){
			logTemplate.log(split, stopwatchLogMessageSource);
		}
	}
	
	
	@Override
	public void onMeterIncrease(Meter meter, long inc, MeterSample sample){
		for(LogTemplate<Meter> logTemplate: getMeterLogTemplates(meter)){
			logTemplate.log(meter, meterLogMessageSource);
		}
	}
	
	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample){
		for(LogTemplate<Counter> logTemplate: getCounterLogTemplates(counter)){
			logTemplate.log(counter, counterLogMessageSource);
		}
	}
	
	
	@Override
	public void onCounterDecrease(Counter counter, long inc, CounterSample sample){
		
		for(LogTemplate<Counter> logTemplate: getCounterLogTemplates(counter)){
			logTemplate.log(counter, counterLogMessageSource);
		}
	}
	
	
}
