package org.flexpay.common.process.filter;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

/**
 * Implements process filtering by name
 */
public class ProcessNameFilter extends PrimaryKeyFilter<ProcessNameObject> {

	private List<ProcessNameObject> processNames = CollectionUtils.list();

	private ProcessManager processManager;

	/**
	 * Loads all unique names of processes in the system as filter content
	 */
	public void loadAllProcessNames() {

		List<String> names = processManager.getAllProcessNames();

		long id = 0L;
		for (String name : names) {
			processNames.add(new ProcessNameObject(id++, name));
		}
	}

	/**
	 * Returns list of loaded process names
	 * @return list of loaded process names
	 */
	public List<ProcessNameObject> getProcessNames() {
		return processNames;
	}

	/**
	 * Returns name of the selected item
	 * @return name of the selected item
	 */
	public String getSelectedName() {

		for (ProcessNameObject nameObject : processNames) {
			if (nameObject.getId().equals(getSelectedId())) {
				return nameObject.getName();
			}
		}

		return null;
	}

	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
