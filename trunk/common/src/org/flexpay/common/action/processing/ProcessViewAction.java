package org.flexpay.common.action.processing;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.LogPreviewUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ProcessViewAction extends FPActionSupport {

    private String logText;
    private ProcessInstance process;
	private Boolean delete;

    private ProcessManager processManager;

    /**
     * Perform action execution.
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return execution result code
     * @throws Exception if failure occurs
     */
    @NotNull
	@Override
    protected String doExecute() throws Exception {

		if (Boolean.TRUE.equals(delete)) {
			log.debug("Try delete process instance {}", process.getId());
			processManager.deleteProcessInstance(process);
			log.debug("Deleted process instance");
		}

        process = processManager.getProcessInstance(process.getId());
		if (process == null) {
			addActionError(getText("common.error.processing.process.process_not_found"));
			return SUCCESS;
		}
        logText = LogPreviewUtil.getLogLastLines(process.getId());

        return SUCCESS;
    }

    /**
     * Get default error execution result
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return {@link #ERROR} by default
     */
    @NotNull
	@Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public ProcessInstance getProcess() {
        return process;
    }

    public void setProcess(ProcessInstance process) {
        this.process = process;
    }

    public String getLogText() {
        return logText;
    }

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	@Required
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

}
