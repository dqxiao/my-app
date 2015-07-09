package com.mycompany.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

//import javax.naming.spi.DirStateFactory.Result;

/**
 * STRING TO DAO OBJECT
 * @author xiaod
 *
 */
public class Message {
	
	private Map<String,String> simonInfo=new HashMap<String,String>();
	
	
	
	public Message(){
	}
	
	public void withInfo(String attrName,String attrVale){
		simonInfo.put(attrName, attrVale);
	}
	
	
	@SuppressWarnings("unchecked")
	public Message(String inputString) {
		
		try{
			byte b[] = inputString.getBytes("ISO-8859-1"); 
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			ObjectInput in = new ObjectInputStream(bis);
			Object o = in.readObject(); 

			simonInfo=(Map<String, String>) o;
		}catch(Exception e){
			e.printStackTrace();

		}
	}

	public String toString(){
		String result=null;
		ObjectOutput out=null;
	    try {
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	out= new ObjectOutputStream(bos);  
	    	out.writeObject(simonInfo);
	    	out.flush();
	    	result=bos.toString("ISO-8859-1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (out != null) {
			      try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
	    
	 
		return result ;
	}
	
	
	public String getAttr(String attrName){
		String result=null;
		if(simonInfo.containsKey(attrName)){
			result=simonInfo.get(attrName);
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getContent(){
		return simonInfo.toString();
	}

}
