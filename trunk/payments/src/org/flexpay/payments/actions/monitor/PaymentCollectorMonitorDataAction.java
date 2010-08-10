package org.flexpay.payments.actions.monitor;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.DISABLE;
import static org.flexpay.payments.actions.monitor.PaymentPointEnableDisableAction.ENABLE;
import static org.flexpay.payments.process.export.TradingDay.PROCESS_STATUS;
import static org.flexpay.payments.process.export.TradingDay.STATUS_CLOSED;
import static org.flexpay.payments.util.MonitorUtils.formatTime;

public class PaymentCollectorMonitorDataAction extends AccountantAWPActionSupport {

    private String updated;
    private String status;
    private String action;

    private ProcessManager processManager;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        PaymentCollector collector = getPaymentCollector();
        if (collector == null) {
            return SUCCESS;
        }

        status = STATUS_CLOSED;
        action = ENABLE;
        Long tradingDayId = collector.getTradingDayProcessInstanceId();
        if (tradingDayId != null && tradingDayId > 0) {
            org.flexpay.common.process.Process process = processManager.getProcessInstanceInfo(tradingDayId);
            log.debug("Found process for paymentCollector with id {} : {}", collector.getId(), process);
            status = process != null ? (String) process.getParameters().get(PROCESS_STATUS) : STATUS_CLOSED;
            action = !status.equals(STATUS_CLOSED) ? DISABLE : ENABLE;
        }

        updated = formatTime.format(new Date());

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public String getUpdated() {
        return updated;
    }

    public String getStatus() {
        return status;
    }

    public String getAction() {
        return action;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }
}
