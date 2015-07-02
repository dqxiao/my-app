package com.mycompany.logging;


public interface QLogger {
	
	public void send(String message);
	public void setConfig(QLoggerConfig config);
	public void info();

	public void close();
}
