package org.flexpay.payments.process.export;

import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.beans.factory.annotation.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.flexpay.common.process.*;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.sorter.ProcessSorterByName;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.service.Roles;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jbpm.graph.exe.ProcessInstance;

import java.util.*;
import java.io.Serializable;

public class TradingDay extends QuartzJobBean {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String PROCESS_DEFINITION_NAME = "TradingDay";
    private static final String USER_TRADING_DAY = "trading-day";
    // time out 10 sec
    private static final long TIME_OUT = 10000;
    private static final ProcessSorterByName processSorterByName = new ProcessSorterByName();

    private ProcessManager processManager;
    private PaymentPointService paymentPointService;

    private List<String> paymentPoints;

    /**
     * Set of authorities names for payments registry
     */
    protected static final List<String> USER_TRADING_DAY_AUTHORITIES = CollectionUtils.list(
            Roles.PROCESS_READ, Roles.PROCESS_DELETE,
            org.flexpay.orgs.service.Roles.PAYMENT_POINT_READ,
            org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
            Roles.PROCESS_DEFINITION_UPLOAD_NEW,
            org.flexpay.orgs.service.Roles.PAYMENT_POINT_CHANGE
    );


    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting traiding day at {}", new Date());

        authenticateTradingDayGenerator();

        try {
            processManager.deployProcessDefinition(PROCESS_DEFINITION_NAME, true);
        } catch (ProcessDefinitionException e) {
            log.error("Deploy exception", e);
            throw new JobExecutionException(e);
        }

        Page<org.flexpay.common.process.Process> page = new Page<org.flexpay.common.process.Process>();
        List<org.flexpay.common.process.Process> processes;
        do {
            processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
            log.debug("Have some processes {}", page.getTotalNumberOfElements());
            try {
                if (processes.size() > 0) {
                    Thread.sleep(TIME_OUT);
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                throw new JobExecutionException(e);
            }

            for (Process process : processes) {
                long processId = process.getId();
                Process processInstanceInfo = processManager.getProcessInstanceInfo(processId);
                log.debug("Process {} state complited {} ", new Object[]{processId, processInstanceInfo.getProcessState().isCompleted()});
                log.debug("Process {} status {} ", new Object[]{processId, processInstanceInfo.getParameters().get("PROCESS_STATUS")});
                log.debug("Process {} payment point {} ", new Object[]{processId, processInstanceInfo.getParameters().get("paymentPointId")});
                long processInstanceId = processInstanceInfo.getProcessInstaceId();
                ProcessInstance pi = processManager.getProcessInstance(processInstanceInfo.getProcessInstaceId());

                log.error("Delete process instace {} ", processInstanceId);
                processManager.deleteProcessInstance(processInstanceInfo);

            }
        } while (processes.size() > 0);
        log.debug("All processes finished");

        for (String paymentPoint : paymentPoints) {
            Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
            Long paymentPointId = Long.parseLong(paymentPoint);
            PaymentPoint pp = paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));
            if (pp == null) {
                log.error("Payment point with id {} not found", paymentPointId);
            } else {

                parameters.put("paymentPointId", paymentPointId);
                log.debug("set paymentPointId {}", paymentPointId);

				//fill begin and end date
                parameters.put("beginDate", DateUtil.truncateDay(new Date()));
                log.debug("set beginDate {}", DateUtil.truncateDay(new Date()));

                parameters.put("endDate", DateUtil.getEndOfThisDay(new Date()));
                log.debug("set endDate {}", DateUtil.getEndOfThisDay(new Date()));

				//@TODO fill organization id
                parameters.put("organizationId", 6L );
                log.debug("set organizationId {}", 6L);



                try {
                    pp.setTradingDayProcessInstanceId(processManager.createProcess(PROCESS_DEFINITION_NAME, parameters));
                    paymentPointService.update(pp);
                } catch (ProcessInstanceException e) {
                    log.error("Failed run process trading day", e);
                    throw new JobExecutionException(e);
                } catch (ProcessDefinitionException e) {
                    log.error("Process trading day not started", e);
                    throw new JobExecutionException(e);
                } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
                    log.error("Payment point did not save", flexPayExceptionContainer);
                    // TODO Kill the process!!!
                }
            }
        }
    }

    @Required
    public void setPaymentPoints(List<String> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    @Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
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
