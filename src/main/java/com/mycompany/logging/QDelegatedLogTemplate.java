package com.mycompany.logging;


public class QDelegatedLogTemplate<C> extends QLogTemplate<C> {

	/** Delegate log template. */
	private final QLogTemplate<C> delegate;

	/**
	 * Constructor with delegate log template.
	 *
	 * @param delegate delegate log template
	 */
	public QDelegatedLogTemplate(QLogTemplate<C> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Get delegate log template.
	 *
	 * @return Delegate log template
	 */
	public QLogTemplate<C> getDelegate() {
		return delegate;
	}

	protected boolean isEnabled(C context) {
		return delegate.isEnabled(context);
	}

	protected void log(String message) {
		delegate.log(message);
	}
	
}
