package com.mycompany.util;


public class Expr {

	private final static String DELIMETER="\\|";
	private String[] patterns;
	private String pattern;
	
	
	public Expr(String pattern){
		this.pattern=pattern;
		patterns=pattern.split(DELIMETER);
	}
	
	
	protected boolean matches(String input){
		boolean match=false;
		for(int i=0;i<patterns.length;i++){
			if(match){
				break;
			}
			match=this.isMatch(input, patterns[i]);
		}
		return match;
	}
	
	/**
	 * Default single Expr matching 
	 * @param s
	 * @param p
	 * @return
	 */
	protected boolean isMatch(String s, String p) {  
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
	
	
	public String toString(){
		return pattern;
	}
	
}
