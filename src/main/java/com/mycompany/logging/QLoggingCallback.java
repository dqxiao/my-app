package com.mycompany.logging;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.CounterSample;
import org.javasimon.Meter;
import org.javasimon.MeterSample;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.callback.logging.LogTemplate;
import org.javasimon.utils.SimonUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.mycompany.util.Message;
import com.mycompany.util.MeterUtil;
/**
 * Logging a set of callback triggered by metric monitors  
 * @author xiaod
 *
 */
public class QLoggingCallback extends CallbackSkeleton {
	
	private Map<String, LogTemplate<Meter>> meterLogTemplates=new HashMap<String, LogTemplate<Meter>>();
	private Map<String,LogTemplate<Counter>> counterLogTemplates=new HashMap<String, LogTemplate<Counter>>();
	private Map<String,LogTemplate<Split>>  stopWatchLogTemplates=new HashMap<String, LogTemplate<Split>>();
	private Map<String, QLoggerFilter> configuration;
	
	
	
	public QLoggingCallback(Collection<QLoggerFilter> qLoggerFilters){
		clear();
		for(QLoggerFilter filter:qLoggerFilters){
			//System.out.printf("filter:%s \n",filter.toString());
			registerByQLoggerFiler(filter);
		}
	}
	
	
	
	private void registerByQLoggerFiler(QLoggerFilter filter){
		if(!configuration.containsKey(filter.getName())){
			configuration.put(filter.getName(),filter);
			registerLogTemplate(filter);
		}else{
			// remove old 
		}
	}
	
	
	
	
	private void registerLogTemplate(QLoggerFilter filter){
		QLogger logger=new QLoggerImpl(new QLoggerConfig(filter.getName()));
		
		meterLogTemplates.put(filter.getName(), new QLogTemplate<Meter>(logger));
		counterLogTemplates.put(filter.getName(), new QLogTemplate<Counter>(logger));
		stopWatchLogTemplates.put(filter.getName(), new QLogTemplate<Split>(logger));
	}
	
	
	private void clear(){
		configuration=new LinkedHashMap<String, QLoggerFilter>();
	}
	
	
	
	
	
	/**Split to string converter*/
	private final LogMessageSource<Split>  stopwatchLogMessageSource = new LogMessageSource<Split>() {
		public String getLogMessage(Split split){
			String TYPE="StopWatch";
			Message message=new Message();
			//
			message.withInfo("timestamp", SimonUtils.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", split.getStopwatch().getName());
			
			// 
			message.withInfo("duration",SimonUtils.presentNanoTime(split.runningFor()));
			return message.toString();
		}
	};
	
	private final LogMessageSource<Counter>  counterLogMessageSource = new LogMessageSource<Counter>() {
		public String getLogMessage(Counter counter){
			String TYPE="Counter";
			Message message=new Message();
			
			// basic information 
			message.withInfo("timestamp", SimonUtils.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", counter.getName());
			
			// detailed information 
			message.withInfo("count", SimonUtils.presentMinMaxCount(counter.getCounter()));
			return message.toString();
		}
	};
	
	
	
	private final LogMessageSource<Meter> meterLogMessageSource = new LogMessageSource<Meter>() {
		public String getLogMessage(Meter meter){
			String TYPE="Meter";
			Message message=new Message();
			
			// basic information 
			message.withInfo("timestamp", SimonUtils.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", meter.getName());
			
			// detailed information 
			message.withInfo("rate", MeterUtil.presentRate(meter.getOneMinuteRate(), TimeUnit.SECONDS));
			message.withInfo("peakRate", MeterUtil.presentRate(meter.getPeakRate(), TimeUnit.SECONDS));
			return message.toString();
		}
	};
	
	
	private Collection<LogTemplate<Split>> getStopwatchLogTemplates(Stopwatch stopwatch){
		Collection<LogTemplate<Split>> result=new ArrayList<LogTemplate<Split>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accept(stopwatch)){
				result.add(stopWatchLogTemplates.get(name));
			}
		}
		return result;
	}

	private Collection<LogTemplate<Meter>> getMeterLogTemplates(Meter meter){
		Collection<LogTemplate<Meter>> result=new ArrayList<LogTemplate<Meter>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accept(meter)){
				result.add(meterLogTemplates.get(name));
			}
		}
		//System.out.printf("size:%d\n",result.size());
		return result;
	}
	
	private Collection<LogTemplate<Counter>> getCounterLogTemplates(Counter counter){
		Collection<LogTemplate<Counter>> result=new ArrayList<LogTemplate<Counter>>();
		for(String name: configuration.keySet()){
			if(configuration.get(name).accept(counter)){
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
//			if(logTemplate==null){
//				System.out.printf("not existing logTempalte");
//			}
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
