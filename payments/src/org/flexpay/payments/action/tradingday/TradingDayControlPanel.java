package org.flexpay.payments.action.tradingday;

import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.payments.service.TradingDay;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.PROCESS_STATUS;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.Status;

public class TradingDayControlPanel {

	private static final String TRANSITIONS = "transitions";

    private static final Logger controlPanelLog = LoggerFactory.getLogger(TradingDayControlPanel.class);

	private List<String> availableCommands = Collections.emptyList();
	private Status processStatus;
	private String command;

	// private data
	private Long tradingDayProcessInstanceId;
	private String actor;

	// process logger
	private Logger userLog;

	private ProcessManager processManager;
	private TradingDay<?> tradingDayService;

	public TradingDayControlPanel() {
	}

	public TradingDayControlPanel(ProcessManager processManager, TradingDay<?> tradingDayService, String actor, Logger userLog) {
		this.processManager = processManager;
		this.userLog = userLog;
		this.actor = actor;
		this.tradingDayService = tradingDayService;
	}

    public void updatePanel(Long tradingDayProcessInstanceId) {

        this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
		processCommand();
		loadAvailableCommands();
		loadProcessStatus();
	}

    public void processCommand(Long tradingDayProcessInstanceId) {
        this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
		processCommand();
	}

	private void processCommand() {

		if (isEmpty(command)) {
			controlPanelLog.info("Empty command. Processing canceled.");
			return;
		}
		
		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process instance id not found. Command processing canceled.");
			return;
		}

		ProcessInstance processInstance = processManager.getProcessInstance(tradingDayProcessInstanceId);
		if (processInstance == null) {
			controlPanelLog.warn("Trading day process instance not found. Command processing canceled.");
			return;
		}

		if (processInstance.getParameters().containsKey(TRANSITIONS) &&
				processInstance.getParameters().get(TRANSITIONS) instanceof List && ((List)processInstance.getParameters().get(TRANSITIONS)).contains(command)) {
			controlPanelLog.debug("Send signal '{}' to {} (actor={})", new Object[]{command, tradingDayProcessInstanceId, actor});
			processManager.completeHumanTask(processInstance, actor, command);
			userLog.debug("Signal '{}' sent to {} (actor={})", new Object[]{command, tradingDayProcessInstanceId, actor});

			command = ""; // reset command
			return;
		}
		controlPanelLog.warn("Can not find '{}' or signal value '{}' on trading day process instance {}",
				new Object[]{TRANSITIONS, command, tradingDayProcessInstanceId});
	}

	@SuppressWarnings({"unchecked"})
	private void loadAvailableCommands() {

		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process instance id not found. Loading available commands canceled.");
			return;
		}

		ProcessInstance processInstance = processManager.getProcessInstance(tradingDayProcessInstanceId);
		if (processInstance == null) {
			controlPanelLog.warn("Trading day process instance not found. Loading available commands canceled.");
			return;
		}

		if (processInstance.getParameters().containsKey(TRANSITIONS) &&
					processInstance.getParameters().get(TRANSITIONS) instanceof List) {
			availableCommands = (List<String>)processInstance.getParameters().get(TRANSITIONS);
		}

        Collections.sort(availableCommands);
	}

	private void loadProcessStatus() {

		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process not found. Status loading canceled.");
            processStatus = Status.CLOSED;
			return;
		}

		ProcessInstance process = processManager.getProcessInstance(tradingDayProcessInstanceId);
		processStatus = process != null ? getStatus(process, Status.CLOSED) :
				PaymentCollectorTradingDayConstants.Status.CLOSED;
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

	public boolean isTradingDayOpened() {
		return tradingDayProcessInstanceId != null && tradingDayService.isOpened(tradingDayProcessInstanceId);
	}
	/*
	private TaskInstance getTaskInstance() {
		return TaskHelper.getTaskInstance(processManager, tradingDayProcessInstanceId, actor, userLog);
	}
       */

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getAvailableCommands() {
		return availableCommands;
	}

	public PaymentCollectorTradingDayConstants.Status getProcessStatus() {
		return processStatus;
	}

	public Long getTradingDayProcessInstanceId() {
		return tradingDayProcessInstanceId;
	}

	public void setTradingDayProcessInstanceId(Long tradingDayProcessInstanceId) {
		this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Logger getUserLog() {
		return userLog;
	}

	public void setUserLog(Logger userLog) {
		this.userLog = userLog;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
