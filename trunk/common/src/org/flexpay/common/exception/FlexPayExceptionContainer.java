package org.flexpay.common.exception;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Container for a FlexPayExceptions
 */
public class FlexPayExceptionContainer extends Exception {

	private Collection<FlexPayException> exceptions = new ArrayList<FlexPayException>();

	/**
	 * Constructs a new FlexPayExceptionContainer.
	 */
	public FlexPayExceptionContainer() {
	}

	/**
	 * Add FlexPayException to the queue
	 *
	 * @param e new FlexPayException
	 */
	public void addException(FlexPayException e) {
		exceptions.add(e);
	}

	/**
	 * Setter for property 'exceptions'.
	 *
	 * @param exceptions Value to set for property 'exceptions'.
	 */
	public void setExceptions(Collection<FlexPayException> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * Getter for property 'exceptions'.
	 *
	 * @return Value for property 'exceptions'.
	 */
	public Collection<FlexPayException> getExceptions() {
		return exceptions;
	}

	/**
	 * Check if container has any exceptions set
	 *
	 * @return <code>true</code> if there is at least one exception in container
	 */
	public boolean isEmpty() {
		return exceptions.isEmpty();
	}
}
