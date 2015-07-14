package com.mycompany.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mycompany.logging.QLogTemplateConfig;
import com.mycompany.logging.QLoggerFilter;


/**
 * ConfigurationReader in XML 
 * 
 * @author xiaod
 *
 */
public class XMLConfigurationReader implements ConfigurationReader{

	private Document doc=null;
	
	public XMLConfigurationReader(String filePath){
		try{
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		}catch(Exception e){
			// Logger issue 
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<QLogTemplateConfig> getQLogTemplateConfig(){
		Collection<QLogTemplateConfig> configs=new ArrayList<QLogTemplateConfig>();
		String typeName="QLogTemplateConfig";
		if(doc==null){
			return configs;
		}
		NodeList nList=doc.getElementsByTagName(typeName);
		//System.out.printf("read QLogTemplateConfig size:%s\n", nList.getLength());
		
		for (int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			
			if(nNode.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			Element eElement = (Element) nNode;
			QLogTemplateConfig config=new QLogTemplateConfig();
			for(Field field: config.getClass().getDeclaredFields()){
				String fieldName=field.getName();
				NodeList fieldList=eElement.getElementsByTagName(fieldName);
				if(fieldList.getLength()!=0){
					String fieldValue=fieldList.item(0).getTextContent();
					//System.out.printf("get %s:%s\n", fieldName,fieldValue);
					try{
						config.getClass().getMethod(setterName(fieldName), String.class).invoke(config, fieldValue);
					}catch(Exception e){
						// Logger this information 
					}
				}
			}
		
			configs.add(config);
		}
		
		return configs;
	}
	
	public Collection<QLoggerFilter> getLoggerFilter(){
		
		Collection<QLoggerFilter> qLoggerFilters=new ArrayList<QLoggerFilter>();
		String typeName="QLoggerFilter";
		if(doc==null){
			return qLoggerFilters;
		}
		
		NodeList nList = doc.getElementsByTagName(typeName);
		
		for (int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			
			if(nNode.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			Element eElement = (Element) nNode;
			QLoggerFilter filter=new QLoggerFilter();
			for(Field field: filter.getClass().getDeclaredFields()){
				String fieldName=field.getName();
				NodeList fieldList=eElement.getElementsByTagName(fieldName);
				if(fieldList.getLength()!=0){
					String fieldValue=fieldList.item(0).getTextContent();
					try{
						filter.getClass().getMethod(setterName(fieldName), String.class).invoke(filter, fieldValue);
					}catch(Exception e){
						// Logger this information 
					}
				}
			}
			qLoggerFilters.add(filter);
		}
		
		return qLoggerFilters;
	}
	
		
	
	private String setterName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
}
