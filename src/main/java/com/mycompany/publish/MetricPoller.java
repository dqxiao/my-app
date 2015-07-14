package com.mycompany.publish;

import java.util.Collection;

import com.mycompany.util.Message;

/**
 * 
 * @author xiaod
 *
 */
public interface MetricPoller {
	/**
	 * Fetch the current messages for a set of metrics that match the provided 
	 * name pattern. This methods should be cheap.
	 * @param namePattern
	 * @return collection of current messages from messagebuffer 
	 */
	Collection<Message> poll(String namePattern);
	
	/**
	 * Fetch the current message for all metrics 
	 * @return
	 */
	Collection<Message> pollStream();
}
