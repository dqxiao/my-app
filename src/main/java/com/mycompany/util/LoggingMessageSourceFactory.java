package com.mycompany.util;

import java.util.concurrent.TimeUnit;

import org.javasimon.Counter;
import org.javasimon.Meter;
import org.javasimon.Split;
import org.javasimon.callback.logging.LogMessageSource;
import org.javasimon.utils.SimonUtils;


/**
 * 
 * @author xiaod
 *
 */
public interface LoggingMessageSourceFactory {
	
	
	LogMessageSource<Counter>  counterLogMessageSource = new LogMessageSource<Counter>() {
		public String getLogMessage(Counter counter){
			String TYPE="Counter";
			Message message=new Message();
			
			// basic information 
			message.withInfo("timestamp", MonitorUtil.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", counter.getName());
			
			// detailed information 
			message.withInfo("count", MonitorUtil.presentMinMaxCount(counter.getCounter()));
			return message.toString();
		}
	};
	
	
	/**
	 * Meter to string converter
	 */
	 LogMessageSource<Meter> meterLogMessageSource = new LogMessageSource<Meter>() {
		public String getLogMessage(Meter meter){
			String TYPE="Meter";
			Message message=new Message();
			
			// basic information 
			message.withInfo("timestamp", MonitorUtil.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", meter.getName());
			
			// detailed information 
			message.withInfo("rate", MonitorUtil.presentRate(meter.getOneMinuteRate(), TimeUnit.SECONDS));
			message.withInfo("peakRate", MonitorUtil.presentRate(meter.getPeakRate(), TimeUnit.SECONDS));
			return message.toString();
		}
	};
	
	
	/**Split to string converter*/
	LogMessageSource<Split>  stopwatchLogMessageSource = new LogMessageSource<Split>() {
		public String getLogMessage(Split split){
			String TYPE="StopWatch";
			Message message=new Message();
			//
			message.withInfo("timestamp", MonitorUtil.presentTimestamp(System.currentTimeMillis()));
			message.withInfo("type", TYPE);
			message.withInfo("name", split.getStopwatch().getName());
			
			// 
			message.withInfo("duration",MonitorUtil.presentNanoTime(split.runningFor()));
			return message.toString();
		}
	};
	
	
	
	
}
