package com.mycompany.logging;

import org.javasimon.clock.SimonClock;

public class QPeriodicLogTemplate<C> extends QDelegatedLogTemplate<C>{


	
	/** Maximum time between two calls to log method. */
	private final long period;

	/** Clock object used to get current time */
	private final SimonClock clock;

	/** Timestamp of next invocation. */
	private long nextTime;

	/**
	 * Constructor with other template and the required period in ms.
	 *
	 * @param delegate concrete log template
	 * @param period logging period in milliseconds
	 */
	public QPeriodicLogTemplate(QLogTemplate<C> delegate, long period) {
		this(delegate, period, SimonClock.SYSTEM);
	}

	public QPeriodicLogTemplate(QLogTemplate<C> delegate, long period, SimonClock clock) {
		// TODO Auto-generated constructor stub
		super(delegate);
		this.period=period;
		this.clock=clock;
		initNextTime();
	}



	/**
	 * Get next invocation time time.
	 *
	 * @return next time
	 */
	public long getNextTime() {
		return nextTime;
	}

	/**
	 * Get current timestamp.
	 *
	 * @return current timestamp
	 */
	long getCurrentTime() {
		return clock.milliTime();
	}

	/** Computes the next timestamp. */
	private synchronized void initNextTime() {
		nextTime = getCurrentTime() + period;
	}

	/** Indicates whether next timestamp is in past. */
	public synchronized boolean isNextTimePassed() {
		return nextTime < getCurrentTime();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return true if delegate is true and enough time passed since last log
	 */
	@Override
	protected boolean isEnabled(C context) {
		
		//boolean result=super.isEnabled(context) && isNextTimePassed();
		//System.out.println("check isEnable Perioridacla:\n"+result+"\n");
		return super.isEnabled(context) && isNextTimePassed();
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Next time is updated after delegate log is called.
	 */
	@Override
	protected void log(String message) {
		
		super.log(message);
		initNextTime();
	}
	
}