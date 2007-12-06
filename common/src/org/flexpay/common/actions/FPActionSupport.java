package org.flexpay.common.actions;

import com.opensymphony.xwork2.ActionSupport;

import java.util.Collection;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

/**
 * Helper ActionSupport extension, able to set 
 */
public class FPActionSupport extends ActionSupport {

	/**
	 * Add several action errors
	 *
	 * @param errorMessages Collection of error messages
	 */
	@SuppressWarnings ({"unchecked"})
	public void addActionErrors(Collection<String> errorMessages) {
		Collection<String> actionErrors = getActionErrors();
		actionErrors.addAll(errorMessages);
		setActionErrors(actionErrors);
	}

	/**
	 * Translate FlexPayException to action error
	 *
	 * @param e FlexPayException
	 */
	public void addActionError(FlexPayException e) {
		addActionError(getText(e.getErrorKey(), e.getParams()));
	}

	/**
	 * Translate several FlexPayExceptions to action errors
	 *
	 * @param container FlexPayExceptionContainer
	 */
	public void addActionErrors(FlexPayExceptionContainer container) {
		for (FlexPayException e : container.getExceptions()) {
			addActionError(e);
		}
	}
}
