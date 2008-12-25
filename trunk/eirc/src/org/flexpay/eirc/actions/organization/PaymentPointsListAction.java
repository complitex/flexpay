package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.PaymentPoint;
import org.flexpay.eirc.persistence.PaymentsCollector;
import org.flexpay.eirc.service.PaymentPointService;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class PaymentPointsListAction extends FPActionSupport {

	private PaymentPointService paymentPointService;

	private PaymentsCollector collector = new PaymentsCollector();
	private List<PaymentPoint> points = Collections.emptyList();
	private Page<PaymentPoint> pager = new Page<PaymentPoint>();

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		if (collector.isNew()) {
			addActionError(getText("error.no_id"));
			return REDIRECT_ERROR;
		}

		points = paymentPointService.listPoints(ApplicationConfig.getDefaultTownStub(), pager);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public PaymentsCollector getCollector() {
		return collector;
	}

	public void setCollector(PaymentsCollector collector) {
		this.collector = collector;
	}

	public List<PaymentPoint> getPoints() {
		return points;
	}

	public Page<PaymentPoint> getPager() {
		return pager;
	}

	public void setPager(Page<PaymentPoint> pager) {
		this.pager = pager;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
