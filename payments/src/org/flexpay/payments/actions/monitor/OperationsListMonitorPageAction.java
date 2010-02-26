package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.BeginTimeFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.EndTimeFilter;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.actions.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.now;

public class OperationsListMonitorPageAction extends OperatorAWPActionSupport {

	private Cashbox cashbox = new Cashbox();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private BeginTimeFilter beginTimeFilter = new BeginTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();

	private ServiceTypeService serviceTypeService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (cashbox == null || cashbox.isNew()) {
			log.warn("Incorrect cashbox id {}", cashbox);
			addActionError(getText("payments.error.cashbox.incorrect_cashbox_id"));
			return REDIRECT_ERROR;
		}

		Stub<Cashbox> stub = stub(cashbox);
		cashbox = cashboxService.read(stub);
		if (cashbox == null) {
			log.warn("Can't get cashbox with id {} from DB", stub.getId());
			addActionError(getText("payments.error.cashbox.cant_get_cashbox"));
			return REDIRECT_ERROR;
		} else if (cashbox.isNotActive()) {
			log.warn("Cashbox with id {} is disabled", stub.getId());
			addActionError(getText("payments.error.cashbox.cant_get_cashbox"));
			return REDIRECT_ERROR;
		}

		Stub<PaymentPoint> paymentPointStub = stub(cashbox.getPaymentPoint());
		PaymentPoint paymentPoint = paymentPointService.read(paymentPointStub);
		if (paymentPoint == null) {
			log.warn("Can't get payment point with id {} from DB", paymentPointStub.getId());
			addActionError(getText("payments.error.payment_point.cant_get_payment_point"));
			return REDIRECT_ERROR;
		}

		initFilters();

		return SUCCESS;
	}

	private void initFilters() {
		beginDateFilter.setDate(now());
		endDateFilter.setDate(now());

		serviceTypeService.initFilter(serviceTypeFilter);
		serviceTypeFilter.setReadOnly(true);
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public Cashbox getCashbox() {
		return cashbox;
	}

	public void setCashbox(Cashbox cashbox) {
		this.cashbox = cashbox;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public EndDateFilter getEndDateFilter() {
		return endDateFilter;
	}

	public BeginTimeFilter getBeginTimeFilter() {
		return beginTimeFilter;
	}

	public EndTimeFilter getEndTimeFilter() {
		return endTimeFilter;
	}

	public ServiceTypeFilter getServiceTypeFilter() {
		return serviceTypeFilter;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

}
