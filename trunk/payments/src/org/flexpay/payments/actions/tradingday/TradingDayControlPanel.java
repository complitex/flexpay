package org.flexpay.payments.actions.tradingday;

import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Transition;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.TradingDay.*;

public class TradingDayControlPanel {

    private static final Logger controlPanelLog = LoggerFactory.getLogger(TradingDayControlPanel.class);

	private List<String> availableCommands = Collections.emptyList();
	private String processStatus;
	private String command;

	// private data
	private Long tradingDayProcessInstanceId;
	private String actor;

	// process logger
	private Logger userLog;

	private ProcessManager processManager;

	public TradingDayControlPanel() {
	}

	public TradingDayControlPanel(ProcessManager processManager, String actor, Logger userLog) {
		this.processManager = processManager;
		this.userLog = userLog;
		this.actor = actor;
	}

    public void updatePanel(@NotNull PaymentPoint paymentPoint) {

        tradingDayProcessInstanceId = paymentPoint.getTradingDayProcessInstanceId();
		processCommand();
		loadAvailableCommands();
		loadProcessStatus();
	}

	private void processCommand() {

		if (isEmpty(command)) {
			controlPanelLog.info("Empty command. Processing canceled.");
			return;
		}
		
		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process not found. Command processing canceled.");
			return;
		}

		TaskInstance taskInstance = getTaskInstance();
		if (taskInstance == null) {
			controlPanelLog.warn("Trading day process task instance not found. Command processing canceled.");
			return;
		}
		final Long taskInstanceId = taskInstance.getId();

		processManager.execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance taskInstance = context.getTaskMgmtSession().getTaskInstance(taskInstanceId);
				if (taskInstance.isSignalling()) {
                    userLog.debug("Signalling {} transition command", command);
					taskInstance.getProcessInstance().signal(command);
				}
				return null;
			}
		});

		command = ""; // reset command
	}

	private void loadAvailableCommands() {

		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process not found. Loading available commands canceled.");
			return;
		}

		final TaskInstance taskInstance = getTaskInstance();
		if (taskInstance == null) {
			controlPanelLog.warn("Trading day process task instance not found. Loading available commands canceled.");
			return;
		}

		availableCommands = processManager.execute(new ContextCallback<List<String>>() {
			@Override
			public List<String> doInContext(@NotNull JbpmContext context) {
				TaskInstance currentTaskInstance = context.getTaskInstance(taskInstance.getId());
				if (currentTaskInstance == null) {
					return Collections.emptyList();
				}

				List<String> availableTransitions = list();
				for (Object o : currentTaskInstance.getProcessInstance().getRootToken().getAvailableTransitions()) {
					Transition t = (Transition) o;
					availableTransitions.add(t.getName());
				}

				return availableTransitions;
			}
		});
	}

	private void loadProcessStatus() {

		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process not found. Status loading canceled.");
            processStatus = STATUS_CLOSED;
			return;
		}

		Process process = processManager.getProcessInstanceInfo(tradingDayProcessInstanceId);
		processStatus = process != null ? (String) process.getParameters().get(PROCESS_STATUS) : STATUS_CLOSED;
	}

	public boolean isTradingDayOpened() {
		return tradingDayProcessInstanceId != null && isOpened(processManager, tradingDayProcessInstanceId, userLog);
	}

	private TaskInstance getTaskInstance() {
		return TaskHelper.getTaskInstance(processManager, tradingDayProcessInstanceId, actor, userLog);
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getAvailableCommands() {
		return availableCommands;
	}

	public String getProcessStatus() {
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
