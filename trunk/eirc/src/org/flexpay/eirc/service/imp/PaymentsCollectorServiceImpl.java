package org.flexpay.eirc.service.imp;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.dao.PaymentsCollectorDao;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.persistence.PaymentsCollectorDescription;
import org.flexpay.eirc.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class PaymentsCollectorServiceImpl
		extends OrganisationInstanceServiceImpl<PaymentsCollectorDescription, PaymentsCollector>
		implements PaymentsCollectorService {

	private PaymentsCollectorDao collectorDao;

	/**
	 * Get I18n error code for error: found several instances
	 *
	 * @return error code
	 */
	protected String getSeveralInstancesErrorCode() {
		return "eirc.error.payments_collector.several_instances";
	}

	/**
	 * Get I18n error code for error: found instance of this type
	 *
	 * @return error code
	 */
	protected String getInstanceExistsErrorCode() {
		return "eirc.error.payments_collector.instance_exists";
	}

	protected void doValidate(@NotNull PaymentsCollector instance, FlexPayExceptionContainer ex) {
		// do nothing
	}

	@Required
	public void setCollectorDao(PaymentsCollectorDao collectorDao) {
		this.collectorDao = collectorDao;
	}
}