package com.mycompany.util;

import org.javasimon.Simon;

public class SimonType {

	public enum SIMONTYPE{
		COUNTER,
		METER,
		STOPWATCH,
		UNKNOW
	}
	
	public static SIMONTYPE getSimonType(Simon simon){
		String className=simon.getClass().getSimpleName();
		SIMONTYPE type=SIMONTYPE.UNKNOW;
		switch (className) {
		case "MeterImpl":
			type=SIMONTYPE.METER;
			break;
		case "CounterImpl":
			type=SIMONTYPE.COUNTER;
			break;
		case "StopWatchImpl":
			type=SIMONTYPE.STOPWATCH;
			break;
		default:
			break;
		}
		return type;
	}
	
}
