package org.flexpay.common.process.filter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.persistence.ProcessDefinition;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

/**
 * Implements process filtering by name
 */
public class ProcessNameFilter extends ObjectFilter {

	private Long selectedId;
	private boolean allowEmpty = true;
	private List<ProcessNameObject> processNames = CollectionUtils.list();

	private ProcessDefinitionManager processDefinitionManager;

	/**
	 * Loads all unique names of processes in the system as filter content
	 */
	public void loadProcessNames() {

		List<ProcessDefinition> definitions = processDefinitionManager.getProcessDefinitions();

		long id = 0L;
		for (ProcessDefinition definition : definitions) {
			processNames.add(new ProcessNameObject(id++, definition.getName()));
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
			if (nameObject.getId().equals(selectedId)) {
				return nameObject.getName();
			}
		}

		return null;
	}

	public Long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	public void setProcessDefinitionManager(ProcessDefinitionManager processManager) {
		this.processDefinitionManager = processManager;
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("selectedName", getSelectedName()).
				append("selectedId", selectedId).
				append("isReadOnly", isReadOnly()).
				append("allowEmpty", allowEmpty).
				toString();
	}

}
