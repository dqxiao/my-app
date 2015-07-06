package com.mycompany.util;

import java.util.concurrent.TimeUnit;
/**
 * 
 * @author xiaod
 *
 */
public final class MeterUtil {

	public static String presentRate(double rate,TimeUnit unit){
		
		String result=String.format("%s events per/%s",
					presentDouble(rate),
					"second");
		
		return result;
	} 
	
	private static String presentDouble(double rate){
		return String.format("%.2f", rate);
	}
}
