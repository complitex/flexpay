package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.jetbrains.annotations.NotNull;

/**
 * Operation that does nothing useful
 */
public class NoneOperation extends Operation {

	/**
	 * ProcessInstance operation
	 *
	 * @param context ProcessingContext 
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {
		return DelayedUpdateNope.INSTANCE;
	}
}
