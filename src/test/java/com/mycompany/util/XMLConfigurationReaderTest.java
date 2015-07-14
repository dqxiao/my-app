package com.mycompany.util;

import java.util.Collection;

import org.junit.Test;

import com.mycompany.logging.QLogTemplateConfig;
import com.mycompany.logging.QLoggerFilter;
import static org.junit.Assert.*;


public class XMLConfigurationReaderTest {
	
	@Test
	public void testgetQLogTemplateConfig(){
		XMLConfigurationReader reader=new XMLConfigurationReader("./test/loggerConfig.xml");
		
		Collection<QLoggerFilter> qLoggerFilters=reader.getLoggerFilter();
		assertEquals(qLoggerFilters.size(), 1);
		
		QLoggerFilter filter=new QLoggerFilter("PhoneHome","*H*","Counter|Meter");
		assertEquals(qLoggerFilters.toArray()[0], filter);
		
	}
	
	@Test 
	public void testgetLoggerFilter(){
		XMLConfigurationReader reader=new XMLConfigurationReader("./test/loggerConfig.xml");
		
		Collection<QLogTemplateConfig> configs=reader.getQLogTemplateConfig();
		
		
//		System.out.printf("configs size:%s\n", configs.size());
		assertEquals(configs.size(), 2);
		
		QLogTemplateConfig configFirst=new QLogTemplateConfig();
		configFirst.setClassName("QLogTemplate");
		configFirst.setNamePattern("*HM*");
		configFirst.setTypePattern("Counter|Meter");
		configFirst.setDuration("10");
		
		
		assertEquals(configFirst, configs.toArray()[0]);
		
		
	}
	
	//@Test 
	
}
