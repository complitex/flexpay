package org.flexpay.payments.actions.monitor;

import org.flexpay.payments.actions.CashboxCookieActionSupport;
import org.flexpay.payments.actions.monitor.data.CashboxMonitorContainer;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.statistics.PaymentsStatisticsService;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.process.export.TradingDay;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.process.ProcessManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.actions.FPActionSupport;
import org.jbpm.graph.def.Transition;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class PaymentPointDetailMonitorAction extends FPActionSupport {
    private static final String PROCESS_STATUS = "PROCESS_STATUS";

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private String name;
    private String paymentsCount;
    private String totalSum;
    private String status;
    private List<String> buttons;
    private String activity;
    private List<CashboxMonitorContainer> cashboxes;
    private String paymentPointId;

    private String update;
    private String updated;

    private CashboxService cashboxService;
    private PaymentPointService paymentPointService;
    private ProcessManager processManager;
    private PaymentsStatisticsService paymentsStatisticsService;
    private OperationService operationService;

    @NotNull
    protected String doExecute() throws Exception {
        if (paymentPointId == null || paymentPointId.length() == 0) {
            log.error("Payment point does not set");
            return ERROR;
        }

        PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(Long.parseLong(paymentPointId)));
        if (paymentPoint == null) {
            log.error("Payment point with id - {} does not exist", paymentPointId);
            return ERROR;
        }

        buttons = new ArrayList<String>();
        
        Long processId = paymentPoint.getTradingDayProcessInstanceId();

        log.debug("processInstanceId = {}", processId);

        if (processId != null && processId > 0) {
            Process process = processManager.getProcessInstanceInfo(processId);
            if (process == null) {
                log.error("Process instance with id - {} does not exist", processId);
                return ERROR;
            }

            String currentStatus = (String) process.getParameters().get(TradingDay.PROCESS_STATUS);

            final long processInstanceId = process.getId();

            Set transitions = TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processInstanceId, activity, log);
            if (status != null && status.equals(currentStatus) && activity != null && activity.length() > 0) {
         //       do {
                process = processManager.getProcessInstanceInfo(processId);
                currentStatus = (String) process.getParameters().get(PROCESS_STATUS);
           //     } while(status.equals(currentStatus));
                transitions = TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, processInstanceId, null, log);
            }
            status = currentStatus;
            for (Object o : transitions) {
                Transition transition = (Transition) o;
                buttons.add(transition.getName());
            }
        }
//----------------------------------------
        List<Cashbox> cbs = cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId());
        Date startDate = DateUtil.now();
        Date finishDate = new Date();

        List<OperationTypeStatistics> statistics = paymentsStatisticsService.operationTypePaymentPointStatistics(Stub.stub(paymentPoint), startDate, finishDate);
        paymentsCount = String.valueOf(getPaymentsCount(statistics));
        totalSum = String.valueOf(getPaymentsSumm(statistics));
        name = paymentPoint.getName(getLocale());

        cashboxes = new ArrayList<CashboxMonitorContainer>();
        if (cbs != null) {
            for (Cashbox cashbox : cbs) {
                statistics = paymentsStatisticsService.operationTypeCashboxStatistics(Stub.stub(cashbox), startDate, finishDate);
                List<Operation> operations = operationService.listLastPaymentOperations(cashbox, startDate, finishDate);
                String lastPayment = operations != null && operations.size() > 0? formatTime.format(operations.get(0).getCreationDate()): null;
				String cashierFio = operations != null && operations.size() > 0 ? operations.get(0).getCashierFio() : null;

                CashboxMonitorContainer container = new CashboxMonitorContainer();
                container.setId(String.valueOf(cashbox.getId()));
                container.setCashbox(cashbox.getName(getLocale()));
                container.setCashierFIO(cashierFio);
                container.setLastPayment(lastPayment);
                container.setPaymentsCount(String.valueOf(getPaymentsCount(statistics)));
                container.setTotalSum(String.valueOf(getPaymentsSumm(statistics)));
                cashboxes.add(container);
            }
        }

        updated = formatTime.format(new Date());

        return SUCCESS;
    }

    @NotNull
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(String paymentsCount) {
        this.paymentsCount = paymentsCount;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public void setButtons(List<String> buttons) {
        this.buttons = buttons;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<CashboxMonitorContainer> getCashboxes() {
        return cashboxes;
    }

    public void setCashboxes(List<CashboxMonitorContainer> cashboxes) {
        this.cashboxes = cashboxes;
    }

    public String getPaymentPointId() {
        return paymentPointId;
    }

    public void setPaymentPointId(String paymentPointId) {
        this.paymentPointId = paymentPointId;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
	}

    @Required
    public void setCashboxService(CashboxService cashboxService) {
        this.cashboxService = cashboxService;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setPaymentsStatisticsService(PaymentsStatisticsService paymentsStatisticsService) {
        this.paymentsStatisticsService = paymentsStatisticsService;
    }

    @Required
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    public long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
		long count = 0;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

	public BigDecimal getPaymentsSumm(List<OperationTypeStatistics> typeStatisticses) {
		BigDecimal summ = BigDecimal.ZERO;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				summ = summ.add(stats.getSumm());
			}
		}
		return summ;
	}
}
