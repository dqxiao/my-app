package com.mycompany.logging;

import org.javasimon.Simon;
import com.mycompany.util.NameExpr;
import com.mycompany.util.TypeExpr;

/**
 * QLoggerFilter
 * Expression whether metric data should be logged or not 
 * @author xiaod
 *
 */
public class QLoggerFilter {
	
	private final static String WILD_START="*";
	private String name;
	private NameExpr namePattern;
	private TypeExpr typePattern;

	
	public QLoggerFilter(){
		this("Default", WILD_START, WILD_START);
	}
	
	public QLoggerFilter(String name,String namePattern,String typePattern){
		this.name=name;
		this.namePattern=new NameExpr(namePattern);
		this.typePattern=new TypeExpr(typePattern);
	}
	
	
	public String getName(){
		return name;
	}
	public NameExpr getNamePattern(){
		return namePattern;
	}
	
	public TypeExpr getTypePattern(){
		return typePattern;
	}
	
	
	public String toString(){
		return String.format("name=%s,namePattern=%s,typePattern=%s ", 
				name,namePattern.toString(),typePattern.toString());
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setNamePattern(String namePattern){
		this.namePattern=new NameExpr(namePattern);
	}
	
	public void setTypePattern(String typePattern){
		this.typePattern=new TypeExpr(typePattern);
	}
	
	public boolean accept(Simon simon){
		return namePattern.accept(simon) && typePattern.accept(simon);
	}
	

}
