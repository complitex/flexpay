package org.flexpay.payments.action.monitor;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.action.AccountantAWPWithPagerActionSupport;
import org.flexpay.payments.action.monitor.data.Command;
import org.flexpay.payments.action.monitor.data.PaymentPointMonitorContainer;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.SecurityUtil.isAuthenticationGranted;
import static org.flexpay.payments.action.tradingday.ProcessTradingDayControlPanelAction.COMMAND_CLOSE;
import static org.flexpay.payments.action.tradingday.ProcessTradingDayControlPanelAction.COMMAND_OPEN;
import static org.flexpay.payments.service.Roles.PAYMENTS_DEVELOPER;
import static org.flexpay.payments.util.MonitorUtils.*;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.*;

public class PaymentPointsListMonitorAction extends AccountantAWPWithPagerActionSupport<PaymentPointMonitorContainer> {

    private List<Command> availableCommands;
    private List<PaymentPointMonitorContainer> paymentPoints;
    private Status processStatus = Status.CLOSED;

    private ProcessManager processManager;
    private OperationService operationService;
    private PaymentsStatisticsService paymentsStatisticsService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
            log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
            addActionError(getText("payments.error.monitor.internal_error"));
            return SUCCESS;
        }

        PaymentCollector paymentCollector = getPaymentCollector();
        if (paymentCollector == null) {
            addActionError(getText("payments.error.monitor.cant_get_payment_collector"));
            return SUCCESS;
        }

		paymentPoints = list();

        for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {

            PaymentPointMonitorContainer container = new PaymentPointMonitorContainer();

            Date startDate = new Date();
            Date finishDate = new Date();
            ProcessInstance tradingDayProcess = null;

            if (paymentPoint.getTradingDayProcessInstanceId() != null) {
				tradingDayProcess = processManager.getProcessInstance(paymentPoint.getTradingDayProcessInstanceId());
                log.debug("Found process for paymentPoint with id {} : {}", paymentPoint.getId(), tradingDayProcess);
			}

            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getStartDate();
                if (tradingDayProcess.getEndDate() != null) {
                    finishDate = tradingDayProcess.getEndDate();
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
            }

            Status status = Status.CLOSED;
            if (tradingDayProcess != null) {
				status = getStatus(tradingDayProcess, status);
            }
            container.setId(paymentPoint.getId());
            container.setName(paymentPoint.getName(getLocale()));
            container.setStatus(status);

            List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(stub(paymentPoint), startDate, finishDate);
            container.setPaymentsCount(getPaymentsCount(statistics));
            container.setTotalSum(String.valueOf(getPaymentsSum(statistics)));

            Operation operation = operationService.getLastPaymentOperationForPaymentPoint(stub(paymentPoint), startDate, finishDate);
            if (operation != null) {
                container.setCashierFIO(operation.getCashierFio());
                container.setCashbox(operation.getCashbox().getName(getLocale()));
                container.setLastPayment(formatTime.format(operation.getCreationDate()));
            }

            paymentPoints.add(container);
        }

        Date startDate = new Date();
        Date finishDate = new Date();
        availableCommands = list();

        if (paymentCollector.getTradingDayProcessInstanceId() != null) {

            ProcessInstance tradingDayProcess = processManager.getProcessInstance(paymentCollector.getTradingDayProcessInstanceId());
            if (tradingDayProcess != null) {
                startDate = tradingDayProcess.getStartDate();
                if (tradingDayProcess.getEndDate() != null) {
                    finishDate = tradingDayProcess.getEndDate();
                }
                processStatus = getStatus(tradingDayProcess, Status.CLOSED);
            }

        }

		if (log.isDebugEnabled()) {
			log.debug("Payment collector trading day status: {}", processStatus);
		}

        if (!Status.CLOSED.equals(processStatus) && !Status.ERROR.equals(processStatus)) {
			if (log.isDebugEnabled()) {
				log.debug("add available command ({}, {})", Transition.CLOSE, COMMAND_CLOSE);
			}
			availableCommands.add(new Command(Transition.CLOSE, COMMAND_CLOSE));
		} else {
            if (isAuthenticationGranted(PAYMENTS_DEVELOPER)) {
				if (log.isDebugEnabled()) {
					log.debug("add available command ({}, {})", Transition.CLOSE, COMMAND_CLOSE);
				}
				availableCommands.add(new Command(Transition.OPEN, COMMAND_OPEN));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Start date={}, finish date={}", formatWithTime(startDate), formatWithTime(finishDate));
        }

        return SUCCESS;
    }

	private Status getStatus(ProcessInstance tradingDayProcess, Status defaultStatus) {
		Object statusObject = tradingDayProcess.getParameters().get(PROCESS_STATUS);
		if (statusObject instanceof Status) {
			defaultStatus = (Status)statusObject;
		} else if (statusObject instanceof String) {
			defaultStatus = PaymentCollectorTradingDayConstants.getStatusByName((String)statusObject);
		}
		return defaultStatus;
	}

	@NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public List<PaymentPointMonitorContainer> getPaymentPoints() {
        return paymentPoints;
    }

    public Status getProcessStatus() {
        return processStatus;
    }

    public List<Command> getAvailableCommands() {
        return availableCommands;
    }

    @Required
    public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
        this.paymentsStatisticsService = paymentsStatisticsService;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

}
