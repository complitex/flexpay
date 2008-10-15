package org.flexpay.common.process;

import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProcessUtil {

	private ProcessManager processManager;

	@NotNull
	public List<Process> findProcesses(@NotNull String definitionName) {
		List<Process> result = CollectionUtils.list();
		List<Process> processes = processManager.getProcessList();
		for (Process process : processes) {
			if (definitionName.equals(process.getProcessDefinitionName())) {
				result.add(process);
			}
		}

		return result;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
