package org.flexpay.payments.process.export;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.flexpay.common.process.*;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.sorter.ProcessSorterByName;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.service.Roles;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

public class TradingDay extends QuartzJobBean {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String PROCESS_DEFINITION_NAME = "TradingDay";
    private static final String USER_TRADING_DAY = "trading-day";
    // time out 10 sec
    private static final long TIME_OUT = 10000;
    private static final ProcessSorterByName processSorterByName = new ProcessSorterByName();

    private ProcessManager processManager;

    private List<String> paymentPoints;

    /**
     * Set of authorities names for payments registry
     */
    protected static final List<String> USER_TRADING_DAY_AUTHORITIES = CollectionUtils.list(
            Roles.PROCESS_READ
    );


    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting traiding day at {}", new Date());

        authenticateTradingDayGenerator();

        Page<org.flexpay.common.process.Process> page = new Page<org.flexpay.common.process.Process>();
        List<org.flexpay.common.process.Process> processes;
        do {
            processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
            try {
                Thread.sleep(TIME_OUT);
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                throw new JobExecutionException(e);
            }
            log.debug("Have some processes {}", page.getTotalNumberOfElements());
        } while (processes != null && processes.size() > 0);
        log.debug("All processes finished");

        for (String paymentPoint : paymentPoints) {
            Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
            parameters.put("paymentPointId", paymentPoint);
            try {
                processManager.createProcess("GeneratePaymentsRegisryProcess", parameters);
            } catch (ProcessInstanceException e) {
                log.error("Failed run process trading day", e);
                throw new JobExecutionException(e);
            } catch (ProcessDefinitionException e) {
                log.error("Process trading day not started", e);
                throw new JobExecutionException(e);
            }
        }
    }

    @Required
    public void setPaymentPoints(List<String> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    /**
     * Do payments registry user authentication
     */
    private static void authenticateTradingDayGenerator() {
        SecurityUtil.authenticate(USER_TRADING_DAY, USER_TRADING_DAY_AUTHORITIES);
    }
}
