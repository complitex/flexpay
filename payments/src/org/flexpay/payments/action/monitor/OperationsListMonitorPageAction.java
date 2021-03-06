package org.flexpay.payments.action.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.*;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.action.OperatorAWPActionSupport;
import org.flexpay.payments.persistence.filters.ServiceTypeFilter;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.now;

public class OperationsListMonitorPageAction extends OperatorAWPActionSupport {

	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private EndDateFilter endDateFilter = new EndDateFilter();
	private BeginTimeWithLimitMinTimeFilter beginTimeFilter = new BeginTimeWithLimitMinTimeFilter();
	private EndTimeFilter endTimeFilter = new EndTimeFilter();
	private ServiceTypeFilter serviceTypeFilter = new ServiceTypeFilter();
    private Cashbox cashbox = new Cashbox();

	private ServiceTypeService serviceTypeService;

	private ProcessManager processManager;

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

		Date beginDate = now();
		Date endDate = now();
		if (paymentPoint.getTradingDayProcessInstanceId() != null) {

            ProcessInstance tradingDayProcess = processManager.getProcessInstance(paymentPoint.getTradingDayProcessInstanceId());
            if (tradingDayProcess != null) {
                beginDate = tradingDayProcess.getStartDate();
                if (tradingDayProcess.getEndDate() != null) {
                    endDate = tradingDayProcess.getEndDate();
                }
            }

        }

		initFilters(beginDate, endDate);

		return SUCCESS;
	}

	private void initFilters(Date beginDate, Date endDate) {
		beginDateFilter.setDate(beginDate);
		endDateFilter.setDate(endDate);

		beginTimeFilter = new BeginTimeWithLimitMinTimeFilter(beginDate, true);

		beginTimeFilter.setMinTime(new BeginTimeFilter(beginDate, true));

		serviceTypeService.initFilter(serviceTypeFilter);
        serviceTypeFilter.setDisabled(true);
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

	public BeginTimeWithLimitMinTimeFilter getBeginTimeFilter() {
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

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
