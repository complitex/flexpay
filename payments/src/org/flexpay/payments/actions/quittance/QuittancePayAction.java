package org.flexpay.payments.actions.quittance;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.persistence.quittance.QuittanceDetailsResponse;
import org.flexpay.payments.service.*;
import org.flexpay.payments.util.ServiceFullIndexUtil;
import org.flexpay.payments.process.export.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class QuittancePayAction extends PaymentOperationAction {

	private static final String TRADING_DAY_CLOSED = "tradingTayClosed"; // tiles result name

	private Operation operation = new Operation();

	// required services
	private OperationService operationService;

	private ProcessManager processManager;

	private PaymentPointService paymentPointService;
	@NotNull
	protected String doExecute() throws Exception {

		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		log.debug("Found cashbox {}", cashbox);
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}
		PaymentPoint paymentPoint = cashbox.getPaymentPoint();
		paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoint));
		final Long paymentProcessId = paymentPoint.getTradingDayProcessInstanceId();
		//@TODO reformat if then else shit
		if (paymentProcessId != null && paymentProcessId != 0) {
			log.debug("Found process id {} for cashbox {}", new Object[]{paymentProcessId, cashboxId});
			if (TradingDay.isOpened(processManager, paymentProcessId, log)) {
				operation = createOperation(cashbox);
				if (BigDecimalUtil.isZero(operation.getOperationSumm()) || operation.getDocuments() == null || operation.getDocuments().size() == 0) {
					log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
				} else {
					operationService.save(operation);
				}
				return REDIRECT_SUCCESS;
			} else {
				return TRADING_DAY_CLOSED;
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("TradingDay process id not found for Payment point id = {}", cashbox.getPaymentPoint().getId());
			}
		}
		operation = createOperation(cashbox);
		if (BigDecimalUtil.isZero(operation.getOperationSumm()) || operation.getDocuments() == null || operation.getDocuments().size() == 0) {
			log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
		} else {
			operationService.save(operation);
		}
		return REDIRECT_SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
