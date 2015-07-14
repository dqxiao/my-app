package com.mycompany.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Wrapping necessary methods for presenting/parse MonitoringUtil 
 * @author xiaod
 *
 */
public class MonitorUtil {
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyMMdd-HHmmss.SSS");
	
	private static final String UNDEF_STRING = "undef";
	private static final int UNIT_PREFIX_FACTOR = 1000;
	private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.US);
	private static final int TEN = 10;
	private static final DecimalFormat UNDER_TEN_FORMAT = new DecimalFormat("0.00", DECIMAL_FORMAT_SYMBOLS);
	private static final int HUNDRED = 100;
	private static final DecimalFormat UNDER_HUNDRED_FORMAT = new DecimalFormat("00.0", DECIMAL_FORMAT_SYMBOLS);
	private static final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("000", DECIMAL_FORMAT_SYMBOLS);

	
	/**
	 * Returns timeStamp in human readable (yet condensed) form "yyMMdd-HHmmss.SSS".
	 *
	 * @param timestamp timestamp in millis
	 * @return timestamp as a human readable string
	 */
	public static String presentTimestamp(long timestamp) {
		if (timestamp == 0) {
			return UNDEF_STRING;
		}
		synchronized (TIMESTAMP_FORMAT) {
			return TIMESTAMP_FORMAT.format(new Date(timestamp));
		}
	}
	
	/**
	 * Return timeStamp inn million seconds from human readable(yet condensed) "yyMMdd-HHmmss.SSS"
	 * @param timeStamp
	 * @return long 
	 */
	public static long parseTimeStamp(String timeStamp){
		long result=0;
		if(timeStamp.equals(UNDEF_STRING)){
			return result;
		}
		
		synchronized (TIMESTAMP_FORMAT) {
			try{
				result =TIMESTAMP_FORMAT.parse(timeStamp).getTime();
				return result;
			}catch(Exception e){
				
			}
		}
		
		return result;	
	}
	
	
	
	
	public static String presentRate(double rate,TimeUnit unit){
		
		String result=String.format("%s events per/%s",
					presentDouble(rate),
					"second");
		
		return result;
	} 
	
	/**
	 * Returns min/max counter values in human readable form - if the value is max or min long value
	 * it is considered unused and string "undef" is returned.
	 *
	 * @param minmax counter extreme value
	 * @return counter value or "undef" if counter contains {@code Long.MIN_VALUE} or {@code Long.MAX_VALUE}
	 */
	public static String presentMinMaxCount(long minmax) {
		if (minmax == Long.MAX_VALUE || minmax == Long.MIN_VALUE) {
			return UNDEF_STRING;
		}
		return String.valueOf(minmax);
	}
	
	private static String presentDouble(double rate){
		return String.format("%.2f", rate);
	}
	
	
	
	
	
	/**
	 * Returns nano-time in human readable form with unit. Number is always from 10 to 9999
	 * except for seconds that are the biggest unit used. In case of undefined value ({@link Long#MAX_VALUE} or
	 * {@link Long#MIN_VALUE}) string {@link #UNDEF_STRING} is returned, so it can be used for min/max values as well.
	 *
	 * @param nanos time in nanoseconds
	 * @return human readable time string
	 */
	public static String presentNanoTime(long nanos) {
		if (nanos == Long.MAX_VALUE || nanos == Long.MIN_VALUE) {
			return UNDEF_STRING;
		}
		return presentNanoTimePrivate((double) nanos);
	}
	
	private static String presentNanoTimePrivate(double time) {
		if (Math.abs(time) < 1d) {
			return "0";
		}

		if (time < UNIT_PREFIX_FACTOR) {
			return ((long) time) + " ns";
		}

		time /= UNIT_PREFIX_FACTOR;
		if (time < UNIT_PREFIX_FACTOR) {
			return formatTime(time, " us");
		}

		time /= UNIT_PREFIX_FACTOR;
		if (time < UNIT_PREFIX_FACTOR) {
			return formatTime(time, " ms");
		}

		time /= UNIT_PREFIX_FACTOR;
		return formatTime(time, " s");
	}
	
	
	private static synchronized String formatTime(double time, String unit) {
		if (time < TEN) {
			return UNDER_TEN_FORMAT.format(time) + unit;
		}
		if (time < HUNDRED) {
			return UNDER_HUNDRED_FORMAT.format(time) + unit;
		}
		return DEFAULT_FORMAT.format(time) + unit;
	}
	

}
