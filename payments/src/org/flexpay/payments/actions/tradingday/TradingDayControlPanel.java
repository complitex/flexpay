package org.flexpay.payments.actions.tradingday;

import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.payments.service.TradingDay;
import org.flexpay.payments.util.PaymentCollectorTradingDayConstants;
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
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.PROCESS_STATUS;
import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.Status;

public class TradingDayControlPanel {

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
			controlPanelLog.warn("Trading day process not found. Command processing canceled.");
			return;
		}

		TaskInstance taskInstance = getTaskInstance();
		if (taskInstance == null) {
			controlPanelLog.warn("Trading day process task instance not found. Command processing canceled.");
			return;
		}
		final Long taskInstanceId = taskInstance.getId();

        userLog.debug("processManager.executing...");

		processManager.execute(new ContextCallback<Void>() {
			@Override
			public Void doInContext(@NotNull JbpmContext context) {
				TaskInstance tInstance = context.getTaskMgmtSession().getTaskInstance(taskInstanceId);
				if (tInstance.isSignalling()) {
                    userLog.debug("Signalling {} transition command", command);
					tInstance.getProcessInstance().signal(command);
				}
				return null;
			}
		});

        userLog.debug("processManager.execute completed");

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
					if (t.getName() != null && !t.getName().startsWith(".")) {
						availableTransitions.add(t.getName());
					}
				}

				return availableTransitions;
			}
		});
	}

	private void loadProcessStatus() {

		if (tradingDayProcessInstanceId == null) {
			controlPanelLog.warn("Trading day process not found. Status loading canceled.");
            processStatus = Status.CLOSED;
			return;
		}

		Process process = processManager.getProcessInstanceInfo(tradingDayProcessInstanceId);
		processStatus = process != null ? (Status) process.getParameters().get(PROCESS_STATUS) :
				PaymentCollectorTradingDayConstants.Status.CLOSED;
	}

	public boolean isTradingDayOpened() {
		return tradingDayProcessInstanceId != null && tradingDayService.isOpened(tradingDayProcessInstanceId);
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
