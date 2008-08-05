package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;

public class SpFileAction extends FPActionSupport {

    private Long spFileId;
    @NonNls
    private String action;

    private ProcessManager processManager;

    @NotNull
	public String doExecute() throws Exception {
        if ("loadToDb".equals(action)) {
            Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
            contextVariables.put("FileId", spFileId);
            processManager.createProcess("ParseRegistryProcess", contextVariables);
        } else if ("loadFromDb".equals(action)) {
            // SzFileUtil.loadFromDb(szFile);
        } else if ("deleteFromDb".equals(action)) {
            // SzFileUtil.deleteRecords(szFile);
        } else if ("fullDelete".equals(action)) {
            // SzFileUtil.delete(szFile);
        }

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

    /**
     * @param spFileId the spFileId to set
     */
    public void setSpFileId(Long spFileId) {
        this.spFileId = spFileId;
    }

    /**
     * @param action the action to set
     */
    public void setAction(@NonNls String action) {
        this.action = action;
    }

    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }
}
