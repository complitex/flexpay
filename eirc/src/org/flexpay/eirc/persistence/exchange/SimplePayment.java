package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;
import java.util.Date;

public class SimplePayment extends Operation {

	private Long organisationId;

	public SimplePayment(List<String> datum) {
		super(Integer.parseInt(datum.get(0)));
	}

	/**
	 * Process payment operation
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws FlexPayException if failure occurs
	 */
	public void process(SpRegistry registry, SpRegistryRecord record) throws FlexPayException {
		Date paymentDate = record.getOperationDate();

		// TODO: process payment
	}

	/**
	 * Get container string representation
	 * TODO: implement me
	 *
	 * @return container string representation
	 */
	public String getStringFormat() {
		return null;
	}
}
