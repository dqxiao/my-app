package com.mycompany.logging;

import java.util.concurrent.TimeUnit;



/**
 * 
 * @author xiaod
 *
 */
public class QLogTemplateConfig {
	private  String namePattern;
	private  String typePattern;
	@SuppressWarnings("unused")
	private  String className;

	private  String duration; 
	private  String timeUnit;

	public QLogTemplateConfig(){
		setNamePattern("*");
		setTypePattern("Meter|Counter|Stopwatch");
		className="QLogTemplate";
		duration="0.0";
		timeUnit="SECONDS";
	}
	
	public void setNamePattern(String namePattern){
		this.namePattern=namePattern;
	} 
	public void setTypePattern(String typePattern){
		this.typePattern=typePattern;
	} 
	
	public void setClassName(String className){
		this.className=className;
	}
	
	public void setDuration(String duration){
		//System.out.printf("set durati:%s\n", timeUnit);
		this.duration=duration;
	}
	
	public void setTimeUnit(String timeUnit){
		System.out.printf("set timeunit:%s\n", timeUnit);
		this.timeUnit=timeUnit;
	}
	

	public String getNamePattern() {
		return namePattern;
	}



	public String getTypePattern() {
		return typePattern;
	}


	

	public double getDurationFactor(){
		
		double result=0.0;
		if(! duration.equals("0.0")){
			int durationVal=Integer.parseInt(duration);
			TimeUnit timeunit=TimeUnit.valueOf(this.timeUnit);
			System.out.println("timeunit:"+timeunit+"\n");
			result=timeunit.toMillis(durationVal);
		}
		
		return result;
	}

	
	
	public String toString(){
		return String.format("className=%s,namePattern=%s,typePattern=%s,duration=%s,timeunit=%s", 
				className,namePattern,typePattern,duration,timeUnit
				);
	}
	
	
	@Override
	public boolean equals(Object other){
		boolean result=false;
		if(other instanceof QLogTemplateConfig){
			if(other.toString().equals(this.toString())){
				result=true;
			}
		}
		
		return result;
	}
	
	
	

}
