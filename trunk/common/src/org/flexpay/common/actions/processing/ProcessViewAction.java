package org.flexpay.common.actions.processing;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.LogPreviewUtil;
import org.jetbrains.annotations.NotNull;

public class ProcessViewAction extends FPActionSupport {

    private String logText;
    private Process process;
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
    protected String doExecute() throws Exception {

        process = processManager.getProcessInstanceInfo(process.getId());
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
    protected String getErrorResult() {
        return SUCCESS;
    }

    /**
     * @return the process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    public String getLogText() {
        return logText;
    }

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }
}
