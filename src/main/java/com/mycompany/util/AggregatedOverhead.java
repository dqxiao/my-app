package com.mycompany.util;


/**
 * 
 * @author xiaod
 *
 */
public class AggregatedOverhead {
	
	private static final double ALPHA = 1.0D - Math.exp(-0.08333333333333333D);
	private Double oldValue;
	private Double maxValue;
	private Double minValue;

	
	
	public AggregatedOverhead(){
		this.oldValue=null;
		this.maxValue=null;
		this.minValue=null;
		
	}
	
	public void observeDiff(long produceTimestamp, long inTimestamp ){
		observe(inTimestamp-produceTimestamp);
	}
	
	public void observe(double value){
		if(oldValue==null){
			oldValue=value;
			maxValue=value;
			minValue=value;
		}else{
			oldValue+=ALPHA*(value-oldValue);
			maxValue=Math.max(maxValue,value);
			minValue=Math.min(minValue,value);
		}
	}

	
	public String statis(){
		return String.format("average=%.2f ms, maxValue=%.2f ms, minValue=%.2f ms",
				oldValue,maxValue,minValue);
	}
}
