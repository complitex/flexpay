package org.flexpay.common.exception;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Container for a FlexPayExceptions, followed to the first 10 exceptions are thrown away
 */
public class FlexPayExceptionContainer extends Exception {

	private Collection<FlexPayException> exceptions = new ArrayList<FlexPayException>();

	public FlexPayExceptionContainer() {
	}

	public FlexPayExceptionContainer(FlexPayException... exs) {
		exceptions.addAll(Arrays.asList(exs));
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

	public void setExceptions(Collection<FlexPayException> exceptions) {
		this.exceptions = exceptions;
	}

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

	public void debug(Logger log) {
		if (log.isDebugEnabled()) {
			for (FlexPayException exception : exceptions) {
				exception.debug(log);
			}
		}
	}

	public void debug(Logger log, String format, Object ... params) {
		if (log.isDebugEnabled()) {
			for (FlexPayException exception : exceptions) {
				exception.debug(log, format, params);
			}
		}
	}

	public void info(Logger log) {
		if (log.isInfoEnabled()) {
			for (FlexPayException exception : exceptions) {
				exception.info(log);
			}
		}
	}

	public void info(Logger log, String format, Object ... params) {
		if (log.isInfoEnabled()) {
			for (FlexPayException exception : exceptions) {
				exception.info(log, format, params);
			}
		}
	}

	public void error(Logger log, String format, Object ... params) {
		for (FlexPayException exception : exceptions) {
			exception.error(log, format, params);
		}
	}
}
