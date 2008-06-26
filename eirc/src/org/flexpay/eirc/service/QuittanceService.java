package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.common.exception.FlexPayExceptionContainer;

public interface QuittanceService {

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws FlexPayExceptionContainer if validation failure occurs
	 */
	void save(QuittanceDetails details) throws FlexPayExceptionContainer;
}
