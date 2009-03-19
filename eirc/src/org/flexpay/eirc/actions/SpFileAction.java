package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

public class SpFileAction extends FPActionSupport {

    private Long spFileId;
	private Long processId = null;
    @NonNls
    private String action;

    private ProcessManager processManager;

    @NotNull
	public String doExecute() throws Exception {

		if (spFileId == null || spFileId <= 0) {
			throw new FlexPayException("Invalid registry id: " + spFileId);
		}

        if ("loadToDb".equals(action)) {
            Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
            contextVariables.put("FileId", spFileId);
            processId = processManager.createProcess("ParseRegistryProcess", contextVariables);
			log.debug("Load to db process id {}", processId);
			if (processId == null) {
				throw new Exception("Failed creating process, unknown reason");
			}

			addActionError(getText("eirc.registry.parse_started"));
        } else if ("loadFromDb".equals(action)) {
            // SzFileUtil.loadFromDb(szFile);
        } else if ("deleteFromDb".equals(action)) {
            // SzFileUtil.deleteRecords(szFile);
        } else if ("fullDelete".equals(action)) {
            // SzFileUtil.delete(szFile);
        }

        return REDIRECT_SUCCESS;
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
        return REDIRECT_ERROR;
    }

    public void setSpFileId(Long spFileId) {
        this.spFileId = spFileId;
    }

    public void setAction(@NonNls String action) {
        this.action = action;
    }

	public Long getProcessId() {
		return processId;
	}

    @Required
	public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

}
