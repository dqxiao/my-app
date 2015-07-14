/**
 * 
 */
package com.mycompany.logging;

/**
 * 
 * @author xiaod
 *
 */
public interface QLogger {
	
	/**
	 * push metric message into message buffer 
	 * @param message
	 */
	public void send(String message);
	/**
	 * setConfiguration about messaging routing 
	 * @param config
	 */
	public void setConfig(QLoggerConfig config);
	/**
	 * propagate work flow information to metric manager 
	 */
	public void info();
	
	/**
	 * 
	 */
	public void start();

	/**
	 * stop working 
	 */
	public void close();
}
