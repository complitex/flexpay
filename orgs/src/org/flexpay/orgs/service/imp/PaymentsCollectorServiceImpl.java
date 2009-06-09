package org.flexpay.orgs.service.imp;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.persistence.PaymentsCollectorDescription;
import org.flexpay.orgs.persistence.filters.OrganizationInstanceFilter;
import org.flexpay.orgs.persistence.filters.PaymentPointsFilter;
import org.flexpay.orgs.persistence.filters.PaymentsCollectorFilter;
import org.flexpay.orgs.service.PaymentsCollectorService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class PaymentsCollectorServiceImpl
		extends OrganizationInstanceServiceImpl<PaymentsCollectorDescription, PaymentsCollector>
		implements PaymentsCollectorService {

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

	/**
	 * Initialize instances filter
	 *
	 * @param filter Instance filter to init
	 * @return Filter back
	 */
	@NotNull
	public PaymentsCollectorFilter initFilter(@NotNull PaymentsCollectorFilter filter) {

		filter.setInstances(listInstances(new Page<PaymentsCollector>(1000, 1)));

		return filter;
	}
}
