package com.mycompany.util;



//import org.javasimon.Counter;
//import org.javasimon.SimonManager;
//
//import com.mycompany.logging.QLoggerFilter;

//import org.apache.log4j.chainsaw.Main;

public class TypeExprTest {
	
	public static void main( String[] args ){
		
//    	String DEFAULT_CONFIG_FILE="./config/loggerConfig.xml";
//    	
//    	for(QLoggerFilter filter: new XMLConfigurationReader(DEFAULT_CONFIG_FILE).getLoggerFilter()){
//    		System.out.println(filter.toString());
//    	}
		
		Message m=new Message();
	
		m.withInfo("has", "a");
		
		String mString=m.toString();
		//System.out.printf("toString %s\n", mString);
		Message m1=new Message(mString);
		
		
		System.out.printf("m1:%s\n", m1.getAttr("has"));
	}

}
