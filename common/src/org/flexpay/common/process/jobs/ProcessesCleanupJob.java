package org.flexpay.common.process.jobs;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.filter.ProcessNameFilter;
import org.flexpay.common.process.filter.ProcessNameObject;
import org.flexpay.common.process.job.Job;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class ProcessesCleanupJob extends Job {

	public static final String PARAM_COMPLETE_BEGIN_TIME = "completeBeginTime";
	public static final String PARAM_COMPLETE_END_TIME = "completeEndTime";
	public static final String PARAM_DEFINITION_NAME = "definitionName";

	private ProcessManager processManager;
	private ProcessDefinitionManager processDefinitionManager;

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		Date beginTime = required(PARAM_COMPLETE_BEGIN_TIME, parameters);
		Date endTime = required(PARAM_COMPLETE_END_TIME, parameters);
		String definitionName = optional(PARAM_DEFINITION_NAME, parameters);

		ProcessNameFilter processNameFilter = new ProcessNameFilter();

		if (definitionName != null) {
			processNameFilter.setProcessDefinitionManager(processDefinitionManager);
			processNameFilter.loadProcessNames();
			for (ProcessNameObject name : processNameFilter.getProcessNames()) {
				if (name.getName().equals(definitionName)) {
					processNameFilter.setSelectedId(name.getId());
				}
			}
		}

		processManager.deleteProcessInstances(new DateRange(beginTime, endTime), processNameFilter);

		return RESULT_NEXT;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setProcessDefinitionManager(ProcessDefinitionManager processDefinitionManager) {
		this.processDefinitionManager = processDefinitionManager;
	}
}
