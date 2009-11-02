package org.flexpay.payments.actions;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.process.export.TradingDay;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Transition;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class TradingDayControlPanel {

	// public data
	private List<String> availableCommands = Collections.emptyList();
	private String processStatus;
	private String command;

	// private data
	private Long tradingDayProcessInstanceId;
	private String actor;

	// own log
	private static final Logger controlPanelLog = LoggerFactory.getLogger(TradingDayControlPanel.class);

	// process logger
	private Logger userLog;

	// required services
	private ProcessManager processManager;

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

		if (StringUtils.isEmpty(command)) {
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
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance taskInstance = context.getTaskMgmtSession().getTaskInstance(taskInstanceId);
				if (taskInstance.isSignalling()) {
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
			public List<String> doInContext(@NotNull JbpmContext context) {
				TaskInstance currentTaskInstance = context.getTaskInstance(taskInstance.getId());
				if (currentTaskInstance == null) {
					return Collections.emptyList();
				}

				List<String> availableTransitions = CollectionUtils.list();
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
			return;
		}

		Process process = processManager.getProcessInstanceInfo(tradingDayProcessInstanceId);
		processStatus = process != null ? (String) process.getParameters().get(TradingDay.PROCESS_STATUS) : null;
	}

	public boolean isTradingDayOpened() {

		return tradingDayProcessInstanceId != null && TradingDay.isOpened(processManager, tradingDayProcessInstanceId, userLog);
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

	// required services
	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
