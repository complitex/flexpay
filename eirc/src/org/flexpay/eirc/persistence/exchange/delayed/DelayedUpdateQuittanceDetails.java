package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

public class DelayedUpdateQuittanceDetails implements DelayedUpdate {

	private QuittanceDetails details;
	private QuittanceService service;

	public DelayedUpdateQuittanceDetails(QuittanceDetails details, QuittanceService service) {
		this.details = details;
		this.service = service;
	}

	/**
	 * Perform storage update
	 *
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if operation fails
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if operation fails
	 */
	@Override
	public void doUpdate() throws FlexPayException, FlexPayExceptionContainer {
		service.save(details);
	}

	@Override
	public int hashCode() {
		return details.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DelayedUpdateQuittanceDetails
				&& ((DelayedUpdateQuittanceDetails) obj).details.equals(details);
	}
}