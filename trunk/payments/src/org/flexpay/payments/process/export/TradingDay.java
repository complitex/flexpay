package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.sorter.ProcessSorterByName;
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
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.getEndOfThisDay;
import static org.flexpay.common.util.DateUtil.now;
import static org.flexpay.common.util.DateUtil.truncateDay;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;

public class TradingDay extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

	// process variable names
	public final static String CAN_UPDATE_OR_CREATE_OPERATION = "CAN_UPDATE_OR_CREATE_OPERATION";
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
    protected static final List<String> USER_TRADING_DAY_AUTHORITIES = list(
            PROCESS_READ,
			PROCESS_DELETE,
			PROCESS_DEFINITION_UPLOAD_NEW,

			PROCESS_DEFINITION_UPLOAD_NEW,
			PROCESS_DEFINITION_UPLOAD_NEW,

            PAYMENT_COLLECTOR_READ,
            PAYMENT_POINT_READ,
			PAYMENT_POINT_CHANGE,
            ORGANIZATION_READ,
            CASHBOX_READ,

			DOCUMENT_READ,
			DOCUMENT_CHANGE,
			OPERATION_READ,
			OPERATION_CHANGE,
			SERVICE_READ

    );

	/**
	 * Return True if Trading Day is opened
	 *
	 * @param processManager process manager instance
	 * @param processInstanceId process instance id
	 * @param logger logger
     * @return true if Trading day is opened or false if not.
	 */
	public static boolean isOpened(@NotNull final ProcessManager processManager,
								   @NotNull final Long processInstanceId, @NotNull final Logger logger){
		return processManager.execute(new ContextCallback<Boolean>(){
            @Override
			public Boolean doInContext(@NotNull JbpmContext context) {
				ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
				if (processInstance == null || processInstance.hasEnded()){
					logger.debug("Process Instance {} was not found", processInstanceId);
					return false;
				}
				String canCreateOrUpdate = (String) context.getProcessInstance(processInstanceId)
						.getContextInstance().getVariable(CAN_UPDATE_OR_CREATE_OPERATION);
				logger.debug("CAN_UPDATE_OR_CREATE_OPERATION = {} for process instance id = {}", canCreateOrUpdate, processInstanceId);
				return Boolean.valueOf(canCreateOrUpdate);
			}
		});
	}

	/**
	 * Main execution method
	 *
	 * @param context - job execution context
	 * @throws JobExecutionException when something goes wrong 
	 */
    @Override
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

        Page<Process> page = new Page<Process>();
        List<Process> processes;
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
                log.debug("Process {} state completed {} ", processId, processInstanceInfo.getProcessState().isCompleted());
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

            if (pp == null) {
                log.error("Payment point is null. Skipping...");
                continue;
            }

            Map<Serializable, Serializable> parameters = map();

            Long paymentPointId = pp.getId();
            parameters.put(ExportJobParameterNames.PAYMENT_POINT_ID, paymentPointId);
            log.debug("Set paymentPointId {}", paymentPointId);

            Date now = now();

            //fill begin and end date
            parameters.put(ExportJobParameterNames.BEGIN_DATE, truncateDay(now));
            log.debug("Set beginDate {}", truncateDay(now));

            parameters.put(ExportJobParameterNames.END_DATE, getEndOfThisDay(now));
            log.debug("Set endDate {}", getEndOfThisDay(now));

            PaymentCollector paymentPointCollector = paymentCollectorService.read(stub(pp.getCollector()));
            parameters.put(ExportJobParameterNames.ORGANIZATION_ID, paymentPointCollector.getOrganization().getId());
            log.debug("Set organizationId {}", paymentPointCollector.getOrganization().getId());

            Long processInstanceId = null;
            try {
                processInstanceId = processManager.createProcess(PROCESS_DEFINITION_NAME, parameters);
                pp.setTradingDayProcessInstanceId(processInstanceId);
                paymentPointService.update(pp);
                log.info("TradingDay process created. Process instance id = {}, for payment point with id {}", processInstanceId, pp.getId());
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
                    log.debug("Delete with processId={}", processInstanceId);
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
