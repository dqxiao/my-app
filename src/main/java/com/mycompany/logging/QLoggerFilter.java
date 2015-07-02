package com.mycompany.logging;


import java.util.ArrayList;
import java.util.List;

import org.javasimon.Simon;

public class QLoggerFilter {
	
	private String name;
	private String namePattern;
	private String typePattern;
	private final static String WILD_START="*";
	private final static String DEFAULT="Default";
	private final static String DELIMETER="\\|";
	
	private List<String> namePatterns=new ArrayList<String>();
	private List<String> typePatterns=new ArrayList<String>();

	
	public boolean isMatch(String s, String p) {  
        int i = 0;  
        int j = 0;  
        int star = -1;  
        int mark = -1;  
        while (i < s.length()) {  
            if (j < p.length()  
                    &&  p.charAt(j) == s.charAt(i)) {  
                ++i;  
                ++j;  
            } else if (j < p.length() && p.charAt(j) == '*') {  
                star = j++;  
                mark = i;  
            } else if (star != -1) {  
                j = star + 1;  
                i = ++mark;  
            } else {  
                return false;  
            }  
        }  
        while (j < p.length() && p.charAt(j) == '*') {  
            ++j;  
        }  
        return j == p.length();  
    } 
	
	
	
	public QLoggerFilter(){
		this(WILD_START,WILD_START);
	}
	
	public QLoggerFilter(String name, String namePattern,String typePattern){
		this.name=name;
		this.namePattern=namePattern;
		this.typePattern=typePattern;
	}
	
	public QLoggerFilter(String namePattern,String typePattern){
		this(DEFAULT,namePattern,typePattern);
		parseNamePatterns();
		parseTypePatterns();
	}

	private void parseTypePatterns(){
		typePatterns.clear();
		for(String typeEx:typePattern.split(DELIMETER)){
			typePatterns.add(typeEx);
		}
	}
	
	
	private void parseNamePatterns(){
		namePatterns.clear();
		for(String nameEx:namePattern.split(DELIMETER)){
			namePatterns.add(nameEx);
		}
	}
	
	private boolean isRightType(Simon simon){
		if(namePattern.equals(WILD_START)){
			return true;
		}
		for(String typeEx:typePatterns){
			if(simon.getClass().getName().contains(typeEx)){
				return true;
			}
		}
		return false;
		
	}
	
	
	private boolean isRightName(Simon simon){
		String simonName=simon.getName();
		for(String nameEx:namePatterns){
			if(isMatch(simonName, nameEx)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean accepted(Simon simon){
		return isRightType(simon) && isRightName(simon);
	}
	
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getNamePattern(){
		return namePattern;
	}
	public void setNamePattern(String namePattern){
		this.namePattern=namePattern;
		parseNamePatterns();
	}
	
	public String getTypePattern(){
		return typePattern;
	}
	public void setTypePattern(String typePattern){
		this.typePattern=typePattern;
		parseTypePatterns();
	}
	
	public String toString(){
		
		
		return String.format("name=%s,namePattern=%s,typePattern=%s", name,namePattern,typePattern);
	}
}
