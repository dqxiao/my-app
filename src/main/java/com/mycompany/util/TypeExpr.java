package com.mycompany.util;

import org.javasimon.Simon;

public class TypeExpr extends Expr {

	public TypeExpr(String pattern) {
		super(pattern);
	}
	
	public boolean accept(Simon simon){
		String simonType=simon.getClass().getSimpleName();
		return matches(simonType);
	}
	@Override
	protected boolean isMatch(String s, String p){
		return s.contains(p);
	}
	
	

}
