package org.flexpay.common.exception;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Container for a FlexPayExceptions, followed to the first 10 exceptions are thrown away 
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
		if (exceptions.size() < 10) {
			exceptions.add(e);
		}
	}

	/**
	 * Add container FlexPayExceptions to the queue
	 *
	 * @param container FlexPayExceptionContainer
	 */
	public void addExceptions(FlexPayExceptionContainer container) {
		for (FlexPayException exception : container.getExceptions()) {
			addException(exception);
		}
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
	 * Check if container has no exceptions set
	 *
	 * @return <code>true</code> if there is no exceptions in container
	 */
	public boolean isEmpty() {
		return exceptions.isEmpty();
	}

	/**
	 * Check if container has any exceptions set
	 *
	 * @return <code>true</code> if there is at least one exception in container
	 */
	public boolean isNotEmpty() {
		return !exceptions.isEmpty();
	}

	public FlexPayException getFirstException() {
		return exceptions.iterator().next();
	}
}
