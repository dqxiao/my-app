package com.mycompany.logging;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Meter;
import org.javasimon.MeterSample;
import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;

import com.mycompany.util.LoggingMessageSourceFactory;

/**
 * Logging a set of callback triggered by metric monitors  
 * @author xiaod
 *  
 */
public class QLoggingCallback extends CallbackSkeleton {
	
	private Map<QLoggerFilter,QLogger> filterLoggers;
	
	private Map<QLoggerFilter,QLogTemplate<Counter>> couterTemplates;
	
	@SuppressWarnings("unused")
	private Map<QLoggerFilter,QLogTemplate<Meter>> meterTemplates;
	
	@SuppressWarnings("unused")
	private Map<QLoggerFilter,QLogTemplate<Split>> stopWatchTemplates;
	 
	
	public QLoggingCallback(){
		clear();
	}
	
	public QLoggingCallback(Collection<QLoggerFilter> filters, Collection<QLogTemplateConfig> configs){
		System.out.printf("init\n");
		clear();
		reigsterLoggers(filters);
		registerTemplates(configs);
	}
	
	
	 
	private void registerTemplates(Collection<QLogTemplateConfig> configs) {
		for(QLogTemplateConfig config:configs){
			long period=(long) config.getDurationFactor();
			QLogTemplate<Counter> counterLogTemplate=new QLogTemplate<Counter>();
			QLogTemplate<Meter> meterLogTemplate=new QLogTemplate<Meter>();
			QLogTemplate<Split> splitLogTemplate=new QLogTemplate<Split>();
			
			QLoggerFilter filter=new QLoggerFilter();
			filter.setNamePattern(config.getNamePattern());
			filter.setTypePattern(config.getTypePattern());
			if(period!=0){
				couterTemplates.put(filter, new QPeriodicLogTemplate<Counter>(counterLogTemplate, period));
				meterTemplates.put(filter,new QPeriodicLogTemplate<>(meterLogTemplate, period));
				stopWatchTemplates.put(filter, new QPeriodicLogTemplate<>(splitLogTemplate, period));
				
			}else{
				couterTemplates.put(filter, counterLogTemplate);
				meterTemplates.put(filter,meterLogTemplate);
				stopWatchTemplates.put(filter, splitLogTemplate);
			}
		}
		
	}

	private void reigsterLoggers(Collection<QLoggerFilter> filters) {
		for(QLoggerFilter filter:filters){
			QLogger logger=new QLoggerImpl(new QLoggerConfig(filter.getName()));
			// unique logger as producer of message buffer 
			logger.start();
			filterLoggers.put(filter, logger);
		}
		
		
	}

	private void clear(){
		filterLoggers=new HashMap<QLoggerFilter,QLogger>();
		couterTemplates=new HashMap<QLoggerFilter,QLogTemplate<Counter>>();
		meterTemplates=new HashMap<QLoggerFilter,QLogTemplate<Meter>>();
		stopWatchTemplates=new HashMap<QLoggerFilter,QLogTemplate<Split>>();
	}
	
	
	
	
	

	
	private  Collection<QLogTemplate<Meter>> getMeterLogTemplates(Meter meter) {
		Collection<QLogTemplate<Meter>> result=new ArrayList<QLogTemplate<Meter>>();
		Collection<QLogger> loggers=new ArrayList<QLogger>();
		getLoggers(meter, loggers);
		//System.out.printf("logger size:%d\n", loggers.size());
		
		for(QLoggerFilter filter: meterTemplates.keySet()){
			if(filter.accept(meter)){
				QLogTemplate<Meter> logTemplate=meterTemplates.get(filter);
				if(logTemplate instanceof QDelegatedLogTemplate<?>){
					for(QLogger logger:loggers){	
						((QDelegatedLogTemplate) logTemplate).getDelegate().setQLogger(logger);
						result.add(logTemplate);
					}
				}else{
					for(QLogger logger:loggers){	
						logTemplate.setQLogger(logger);
						result.add(logTemplate);
					}
				}
			}
		}
		
		return result;
	}

	
	private Collection<LogTemplate<Split>> getStopwatchLogTemplates(Stopwatch stopwatch) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Collection<QLogTemplate<Counter>> getCounterLogTemplates(Counter counter) {
		
		Collection<QLogTemplate<Counter>> result=new ArrayList<QLogTemplate<Counter>>();
		Collection<QLogger> loggers=new ArrayList<QLogger>();
		getLoggers(counter, loggers);
		//System.out.printf("logger size:%d\n", loggers.size());
		
		for(QLoggerFilter filter: couterTemplates.keySet()){
			if(filter.accept(counter)){
				QLogTemplate<Counter> logTemplate=couterTemplates.get(filter);
				if(logTemplate instanceof QDelegatedLogTemplate<?>){
					for(QLogger logger:loggers){	
						((QDelegatedLogTemplate) logTemplate).getDelegate().setQLogger(logger);
						result.add(logTemplate);
					}
				}else{
					for(QLogger logger:loggers){	
						logTemplate.setQLogger(logger);
						result.add(logTemplate);
					}
				}
			}
		}
		
		
		
		return result;
	}

	
	
	
	private void getLoggers(Simon simon,Collection<QLogger> loggers){
		
		for(QLoggerFilter filter: filterLoggers.keySet()){
			if(filter.accept(simon)){
				loggers.add(filterLoggers.get(filter));
			}
		}
	}
	

	@Override
	public void onMeterIncrease(Meter meter, long inc, MeterSample sample){
		for(LogTemplate<Meter> logTemplate: getMeterLogTemplates(meter)){
			logTemplate.log(meter, LoggingMessageSourceFactory.meterLogMessageSource);
		}
	}
	
	@Override 
	public void onMeterDecrease(Meter meter, long inc, MeterSample paramMeterSample) {
		for(QLogTemplate<Meter> logTemplate: getMeterLogTemplates(meter)){
			if(logTemplate!=null){
				logTemplate.log(meter, LoggingMessageSourceFactory.meterLogMessageSource);
			}
		}
	};
	
	@Override
	public void onCounterIncrease(Counter counter, long inc, CounterSample sample){
		for(QLogTemplate<Counter> logTemplate: getCounterLogTemplates(counter)){
			if(logTemplate!=null){
				logTemplate.log(counter,LoggingMessageSourceFactory.counterLogMessageSource );
			}
		}
	}
	
	
	@Override
	public void onCounterDecrease(Counter counter, long inc, CounterSample sample){
		for(QLogTemplate<Counter> logTemplate: getCounterLogTemplates(counter)){
			if(logTemplate!=null){
				logTemplate.log(counter, LoggingMessageSourceFactory.counterLogMessageSource);
			}
		}
	}

	
	@Override
	public void onStopwatchStop(Split split, StopwatchSample sample) {
		for(LogTemplate<Split> logTemplate: getStopwatchLogTemplates(split.getStopwatch())){
			if(logTemplate!=null){
				logTemplate.log(split, LoggingMessageSourceFactory.stopwatchLogMessageSource);
			}
		}
	}
	
	
}
