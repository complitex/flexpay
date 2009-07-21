package org.flexpay.payments.actions.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.BigDecimalUtil;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.PaymentOperationAction;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.*;
import org.flexpay.payments.process.export.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;

public class QuittancePayAction extends PaymentOperationAction {

	private static final String TRADING_DAY_CLOSED = "tradingDayClosed"; // tiles result name

	private Long operationBlankId;

	// required services
	private ProcessManager processManager;
	private OperationService operationService;
	private PaymentPointService paymentPointService;

	@NotNull
	protected String doExecute() throws Exception {

		Operation operation = operationService.read(new Stub<Operation>(operationBlankId));
		
		Cashbox cashbox = getCashbox();
		final Long paymentProcessId = getPaymentProcessId(cashbox);

		if (paymentProcessId == null || paymentProcessId == 0) {
			log.debug("TradingDay process id not found for Payment point id = {}", cashbox.getPaymentPoint().getId());

			fillOperation(operation, cashbox);
			if (!isValidOperation(operation)) {
				addActionError(getText("payments.error.operation_is_incorrect"));
				return REDIRECT_SUCCESS;
			}

			if (isNotEmptyOperation(operation)) {
				operationService.save(operation);
			} else {
				log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
			}

			return REDIRECT_SUCCESS;

		} else {
			log.debug("Found process id {} for cashbox {}", new Object[]{paymentProcessId, cashboxId});

			if (!TradingDay.isOpened(processManager, paymentProcessId, log)) {
				return TRADING_DAY_CLOSED;
			}

			fillOperation(operation, cashbox);
			if (!isValidOperation(operation)) {
				addActionError(getText("payments.error.operation_is_incorrect"));
				return REDIRECT_SUCCESS;
			}

			if (isNotEmptyOperation(operation)) {
				operationService.save(operation);
			} else {
				log.debug("Zero summ for operation or zero documents for operation created. Operation was not created");
			}

			return REDIRECT_SUCCESS;
		}
	}

	private Long getPaymentProcessId(Cashbox cashbox) {
		PaymentPoint paymentPoint = cashbox.getPaymentPoint();
		paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoint));
		return paymentPoint.getTradingDayProcessInstanceId();
	}

	private Cashbox getCashbox() {
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		log.debug("Found cashbox {}", cashbox);
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}
		return cashbox;
	}

	private boolean isNotEmptyOperation(Operation operation) {
		return !BigDecimalUtil.isZero(operation.getOperationSumm()) && operation.getDocuments() != null && operation.getDocuments().size() > 0;
	}

	private boolean isValidOperation(Operation operation) {

		BigDecimal totalSumm = operation.getOperationSumm();
		BigDecimal inputSumm = operation.getOperationInputSumm();
		BigDecimal changeSumm = operation.getChange();

		BigDecimal summ = totalSumm.add(changeSumm);

		if (inputSumm == null || totalSumm == null || changeSumm == null) {
			return false;
		}

		if (inputSumm.compareTo(summ) != 0) {
			return false;
		}

		return true;
	}

	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public void setOperationBlankId(Long operationBlankId) {
		this.operationBlankId = operationBlankId;
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
