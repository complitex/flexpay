package org.flexpay.payments.action.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.action.PaymentOperationAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;

import static org.flexpay.common.util.BigDecimalUtil.isNotZero;

public class QuittancePayAction extends PaymentOperationAction {

	private static final String TRADING_DAY_CLOSED = "tradingDayClosed"; // tiles result name

	private Long operationId;

	private TradingDay<Cashbox> cashboxTradingDayService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (operationId == null || operationId <= 0) {
            log.warn("operationId parameter is incorrect");
			addActionError(getText("payments.error.operation_is_incorrect"));
			return REDIRECT_SUCCESS;
		}

		Operation operation = operationService.read(new Stub<Operation>(operationId));
		if (operation == null) {
            log.warn("Can't get operation with id {} from DB", operationId);
			addActionError(getText("payments.error.operation_is_incorrect"));
			return REDIRECT_SUCCESS;
		}

		Cashbox cashbox = getCashbox();
		final Long cashboxProcessId = cashbox.getTradingDayProcessInstanceId();

		if (cashboxProcessId == null || cashboxProcessId == 0) {
			log.debug("TradingDaySchedulingJob process id not found for Cashbox id = {}", cashbox.getPaymentPoint().getId());
            return TRADING_DAY_CLOSED;
		} else {
			log.debug("Found process id {} for cashbox {}", cashboxProcessId, getCashboxId());
            if (!cashboxTradingDayService.isOpened(cashbox.getTradingDayProcessInstanceId())) {
                return TRADING_DAY_CLOSED;
            }
		}

		fillOperation(operation);
		if (!validateOperation(operation)) {
            log.warn("Invalid operation with id {}", operationId);
			addActionError(getText("payments.error.operation_is_incorrect"));
			return REDIRECT_SUCCESS;
		}

        log.debug("Fill new pay operation {}", operation);

		if (isNotEmptyOperation(operation)) {
            operationService.update(operation);
		} else {
			log.debug("Zero sum for operation or zero documents for operation created. Operation was not created");
		}

		return REDIRECT_SUCCESS;

	}

	private Cashbox getCashbox() {
		Long cashboxId = getCashboxId();
		Cashbox cashbox = cashboxService.read(new Stub<Cashbox>(cashboxId));
		log.debug("Found cashbox {}", cashbox);
		if (cashbox == null) {
			throw new IllegalArgumentException("Invalid cashbox id: " + cashboxId);
		}
		return cashbox;
	}

	private boolean isNotEmptyOperation(Operation operation) {
		return isNotZero(operation.getOperationSum()) && operation.getDocuments() != null && !operation.getDocuments().isEmpty();
	}

	private boolean validateOperation(Operation operation) {

		BigDecimal totalSum = operation.getOperationSum();
		BigDecimal inputSum = operation.getOperationInputSum();
		BigDecimal changeSum = operation.getChange();

		BigDecimal sum = totalSum.add(changeSum);

		if (inputSum == null || totalSum == null || changeSum == null) {
			return false;
		}

		return inputSum.compareTo(sum) == 0;

	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	@Required
	public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
		this.cashboxTradingDayService = cashboxTradingDayService;
	}
}
