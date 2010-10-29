package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.actions.monitor.data.CashboxMonitorContainer;
import org.flexpay.payments.actions.monitor.data.Command;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.actions.tradingday.ProcessTradingDayControlPanelAction.COMMAND_UNMARK_CLOSE_DAY;
import static org.flexpay.payments.actions.tradingday.ProcessTradingDayControlPanelAction.COMMAND_MARK_CLOSE_DAY;
import static org.flexpay.payments.util.MonitorUtils.*;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.*;

public class PaymentPointCashboxesListAction extends AccountantAWPWithPagerActionSupport<CashboxMonitorContainer> {

    private List<Command> availableCommands;
	private PaymentPoint paymentPoint = new PaymentPoint();
	private List<CashboxMonitorContainer> cashboxes = list();
    private Statuses processStatus = Statuses.CLOSED;

	private ProcessManager processManager;
	private OperationService operationService;
    private CashboxService cashboxService;
	private PaymentPointService paymentPointService;
	private PaymentsStatisticsService paymentsStatisticsService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (paymentPoint == null || paymentPoint.isNew()) {
			log.warn("Incorrect payment point id {}", paymentPoint);
			addActionError(getText("payments.payment_point.detail.error.pp.does_not_set"));
			return ERROR;
		}

		Stub<PaymentPoint> stub = stub(paymentPoint);
		paymentPoint = paymentPointService.read(stub);
		if (paymentPoint == null) {
			log.warn("Can't get Payment point with id {} from DB", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		} else if (paymentPoint.isNotActive()) {
			log.warn("Payment point with id {} is disabled", stub.getId());
			addActionError(getText("payments.payment_point.detail.error.inner_error"));
			return ERROR;
		}

        Date startDate = new Date();
        Date finishDate = new Date();
        availableCommands = list();

        if (paymentPoint.getTradingDayProcessInstanceId() != null) {

            Process tradingDayProcess = processManager.getProcessInstanceInfo(paymentPoint.getTradingDayProcessInstanceId());
            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getProcessStartDate();
                if (tradingDayProcess.getProcessEndDate() != null) {
                    finishDate = tradingDayProcess.getProcessEndDate();
                }
                processStatus = (Statuses) tradingDayProcess.getParameters().get(PROCESS_STATUS);
            }

        }

        if (Statuses.WAIT_APPROVE.equals(processStatus)) {
            availableCommands.add(new Command(Transitions.MARK_CLOSE_DAY, COMMAND_MARK_CLOSE_DAY));
            availableCommands.add(new Command(Transitions.UNMARK_CLOSE_DAY, COMMAND_UNMARK_CLOSE_DAY));
        } else if (Statuses.OPEN.equals(processStatus)) {
            availableCommands.add(new Command(Transitions.MARK_CLOSE_DAY, COMMAND_MARK_CLOSE_DAY));
        }

        if (log.isDebugEnabled()) {
            log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
        }

		List<Cashbox> cbs = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());
		for (Cashbox cashbox : cbs) {

			CashboxMonitorContainer container = new CashboxMonitorContainer();
			container.setId(cashbox.getId());
			container.setCashbox(cashbox.getName(getLocale()));

            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypeCashboxStatistics(stub(cashbox), startDate, finishDate);
			container.setPaymentsCount(getPaymentsCount(statistics));
			container.setTotalSum(String.valueOf(getPaymentsSum(statistics)));

            Operation operation = operationService.getLastPaymentOperationsForCashbox(stub(cashbox), startDate, finishDate);
            if (operation != null) {
                container.setCashierFIO(operation.getCashierFio());
                container.setLastPayment(formatTime.format(operation.getCreationDate()));
            }
			if (cashbox.getTradingDayProcessInstanceId() != null) {
				Process process = processManager.getProcessInstanceInfo(cashbox.getTradingDayProcessInstanceId());
				if (process != null) {
					container.setStatus((PaymentCollectorTradingDayConstants.Statuses)process.getParameters().get(PaymentCollectorTradingDayConstants.PROCESS_STATUS));
				}
			}

			cashboxes.add(container);
		}

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
	@Override
	protected String getErrorResult() {
		return ERROR;
	}

	public List<CashboxMonitorContainer> getCashboxes() {
		return cashboxes;
	}

	public PaymentPoint getPaymentPoint() {
		return paymentPoint;
	}

	public void setPaymentPoint(PaymentPoint paymentPoint) {
		this.paymentPoint = paymentPoint;
	}

    public PaymentCollectorTradingDayConstants.Statuses getProcessStatus() {
        return processStatus;
    }

    public List<Command> getAvailableCommands() {
        return availableCommands;
    }

    @Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setOperationService(OperationService operationService) {
		this.operationService = operationService;
	}

	@Required
	public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
		this.paymentsStatisticsService = paymentsStatisticsService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
