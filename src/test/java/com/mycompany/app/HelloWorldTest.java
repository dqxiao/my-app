package com.mycompany.app;
import java.util.concurrent.TimeUnit;


public class HelloWorldTest {
	
	public static void main(String[] args){
		
		String timeString="SECONDS";
		TimeUnit timeUnit = TimeUnit.valueOf(timeString);
		System.out.println(timeUnit.toMillis(5));
		
	}

}
