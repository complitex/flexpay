package org.flexpay.orgs.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.persistence.filters.PaymentCollectorFilter;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class PaymentCollectorServiceImpl
		extends OrganizationInstanceServiceImpl<PaymentCollectorDescription, PaymentCollector>
		implements PaymentCollectorService {

	/**
	 * Get I18n error code for error: found several instances
	 *
	 * @return error code
	 */
	protected String getSeveralInstancesErrorCode() {
		return "orgs.error.payment_collector.several_instances";
	}

	/**
	 * Get I18n error code for error: found instance of this type
	 *
	 * @return error code
	 */
	protected String getInstanceExistsErrorCode() {
		return "orgs.error.payment_collector.instance_exists";
	}

	protected void doValidate(@NotNull PaymentCollector instance, FlexPayExceptionContainer ex) {
		// do nothing
	}

	/**
	 * Initialize instances filter
	 *
	 * @param filter Instance filter to init
	 * @return Filter back
	 */
	@NotNull
	public PaymentCollectorFilter initFilter(@NotNull PaymentCollectorFilter filter) {

		filter.setInstances(listInstances(new Page<PaymentCollector>(1000, 1)));
		return filter;
	}
}
