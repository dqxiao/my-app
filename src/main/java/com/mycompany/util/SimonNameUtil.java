package com.mycompany.util;

import java.util.HashMap;
import java.util.Map;
//import com.google.common.base.Joiner;
/**
 * Idea from  http://techblog.netflix.com/2014/12/introducing-atlas-netflixs-primary.html
 * @author xiaod
 *
 */
public class SimonNameUtil {
	
	private Map<String,String> IDSets;
	
	/**
	 * Default setting of information 
	 */
	public SimonNameUtil(){
		IDSets=new HashMap<String,String>();
		IDSets.put("product", "NSX");
		IDSets.put("component","MP");
	}
	
	public void appendAnnotation(String key, String value){
		if(IDSets.containsKey(key)){
			IDSets.replace(key, IDSets.get(key), value);
		}else{
			IDSets.put(key, value);
		}
	}
	
	
	
	// transfer 
	public String toString(){
		return null;
		
		
//		String result = Joiner.on("|").withKeyValueSeparator("_").join(IDSets);
		//return result;
	
	}
}
