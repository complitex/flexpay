package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.sorter.ProcessSorterByName;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.process.export.job.ExportJobParameterNames;
import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradingDay extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

	// process variable names
	public final static String CAN_UPDATE_OR_CRETAE_OPERATION = "CAN_UPDATE_OR_CRETAE_OPERATION";
	public final static String PROCESS_STATUS = "PROCESS_STATUS";
	public final static String AUTO_MODE = "AUTO_MODE";

    private static final String PROCESS_DEFINITION_NAME = "TradingDay";
    private static final String USER_TRADING_DAY = "trading-day";
    // time out 10 sec
    private static final long TIME_OUT = 10000;
    private static final ProcessSorterByName processSorterByName = new ProcessSorterByName();


    private ProcessManager processManager;
    private PaymentPointService paymentPointService;
    private PaymentCollectorService paymentCollectorService;

    /**
     * Set of authorities names for payments registry
     */
    protected static final List<String> USER_TRADING_DAY_AUTHORITIES = CollectionUtils.list(
            org.flexpay.common.service.Roles.PROCESS_READ,
			org.flexpay.common.service.Roles.PROCESS_DELETE,
			org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW,

			org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW,
			org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW,

            org.flexpay.orgs.service.Roles.PAYMENT_COLLECTOR_READ,
            org.flexpay.orgs.service.Roles.PAYMENT_POINT_READ,
			org.flexpay.orgs.service.Roles.PAYMENT_POINT_CHANGE,
            org.flexpay.orgs.service.Roles.ORGANIZATION_READ,
            org.flexpay.orgs.service.Roles.CASHBOX_READ,

			org.flexpay.payments.service.Roles.DOCUMENT_READ,
			org.flexpay.payments.service.Roles.DOCUMENT_CHANGE,
			org.flexpay.payments.service.Roles.OPERATION_READ,
			org.flexpay.payments.service.Roles.OPERATION_CHANGE,
			org.flexpay.payments.service.Roles.SERVICE_READ

    );

	/**
	 * Return True if Trading Day is opened
	 *
	 * @param processManager process manager instance
	 * @param processInstanceId process instance id
	 * @return true if Trading day is opened or false if not.
	 */
	public static boolean isOpened(@NotNull final ProcessManager processManager,
								   @NotNull final Long processInstanceId, @NotNull final Logger logger){
		return processManager.execute(new ContextCallback<Boolean>(){
			public Boolean doInContext(@NotNull JbpmContext context) {
				ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
				if (processInstance == null || processInstance.hasEnded()){
					logger.debug("Process Instance {} was not found", processInstanceId);
					return false;
				}
				String canCreateOrUpdate = (String)context.getProcessInstance(processInstanceId)
						.getContextInstance().getVariable(TradingDay.CAN_UPDATE_OR_CRETAE_OPERATION);
				logger.debug("CAN_UPDATE_OR_CRETAE_OPERATION = {} for process instance id = {}",
						new Object[]{canCreateOrUpdate, processInstanceId});
				return new Boolean(canCreateOrUpdate);
			}
		});
	}

	/**
	 * Main execution method
	 *
	 * @param context - job execution context
	 * @throws JobExecutionException when something goes wrong 
	 */
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting trading day at {}", new Date());

        authenticateTradingDayGenerator();

        /*
        // Redeploy process definition. Using in development.
        try {
            processManager.deployProcessDefinition(PROCESS_DEFINITION_NAME, true);
        } catch (ProcessDefinitionException e) {
            log.error("Deploy exception", e);
            throw new JobExecutionException(e);
        }
        */

        Page<org.flexpay.common.process.Process> page = new Page<org.flexpay.common.process.Process>();
        List<org.flexpay.common.process.Process> processes;
        do {
            processes = processManager.getProcesses(processSorterByName, page, null, null, ProcessState.RUNING, PROCESS_DEFINITION_NAME);
            log.debug("Have some processes {}", page.getTotalNumberOfElements());
            try {
                if (!processes.isEmpty()) {
                    Thread.sleep(TIME_OUT);
                }
            } catch (InterruptedException e) {
                log.warn("Interrupted", e);
                throw new JobExecutionException(e);
            }

            for (Process process : processes) {
                long processId = process.getId();
                Process processInstanceInfo = processManager.getProcessInstanceInfo(processId);
                log.debug("Process {} state complited {} ", processId, processInstanceInfo.getProcessState().isCompleted());
                log.debug("Process {} status {} ", processId, processInstanceInfo.getParameters().get(PROCESS_STATUS));
                log.debug("Process {} payment point {} ", processId, processInstanceInfo.getParameters().get(ExportJobParameterNames.PAYMENT_POINT_ID));
                //long processInstanceId = processInstanceInfo.getProcessInstaceId();
                //ProcessInstance pi = processManager.getProcessInstance(processInstanceInfo.getProcessInstaceId());

                //log.error("Delete process instace {} ", processInstanceId);
                //processManager.deleteProcessInstance(processInstanceInfo);

            }
        } while (!processes.isEmpty());
        log.debug("All processes finished");

        for (PaymentPoint pp : paymentPointService.listPaymentPointsWithTradingDay()) {
            Long paymentPointId = pp.getId();
            Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
            if (pp == null) {
                log.error("Payment point with id {} not found", paymentPointId);
            } else {

                parameters.put(ExportJobParameterNames.PAYMENT_POINT_ID, paymentPointId);
                log.debug("Set paymentPointId {}", paymentPointId);

				//fill begin and end date
                parameters.put(ExportJobParameterNames.BEGIN_DATE, DateUtil.truncateDay(new Date()));
                log.debug("Set beginDate {}", DateUtil.truncateDay(new Date()));

                parameters.put(ExportJobParameterNames.END_DATE, DateUtil.getEndOfThisDay(new Date()));
                log.debug("Set endDate {}", DateUtil.getEndOfThisDay(new Date()));

                PaymentCollector paymentPointCollector = paymentCollectorService.read(Stub.stub(pp.getCollector()));
                parameters.put(ExportJobParameterNames.ORGANIZATION_ID, paymentPointCollector.getOrganization().getId());
                log.debug("Set organizationId {}", paymentPointCollector.getOrganization().getId());

                Long processInstanceId = null;
                try {
                    processInstanceId = processManager.createProcess(PROCESS_DEFINITION_NAME, parameters);
                    pp.setTradingDayProcessInstanceId(processInstanceId);
                    paymentPointService.update(pp);
                } catch (ProcessInstanceException e) {
                    log.error("Failed run process trading day", e);
                    throw new JobExecutionException(e);
                } catch (ProcessDefinitionException e) {
                    log.error("Process trading day not started", e);
                    throw new JobExecutionException(e);
                } catch (Throwable th) {
                    log.error("Payment point did not save", th);
                    if (processInstanceId != null) {
                        deleteProcess(processInstanceId);
                        log.debug("Delete processId={}", processInstanceId);
                    }
                }
            }
        }
    }

    private void deleteProcess(long processInstanceId) {
        Process process = processManager.getProcessInstanceInfo(processInstanceId);
        if (process != null) {
            processManager.deleteProcessInstance(process);
        }
    }

	/**
	 * Do payments registry user authentication
	 */
	private static void authenticateTradingDayGenerator() {
		SecurityUtil.authenticate(USER_TRADING_DAY, USER_TRADING_DAY_AUTHORITIES);
	}

	@Required
    public void setPaymentPointService(PaymentPointService paymentPointService) {
        this.paymentPointService = paymentPointService;
    }

    @Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Required
    public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
        this.paymentCollectorService = paymentCollectorService;
    }
}
