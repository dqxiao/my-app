package com.mycompany.util;

import org.javasimon.Simon;


public final class NameExpr extends Expr {

	public NameExpr(String pattern) {
		super(pattern);
	}
	
	
	public boolean accept(Simon simon){
		String simonName=simon.getName();
		return matches(simonName);
	}
	
	public boolean acceptName(String name){
		return matches(name);
	}
}
