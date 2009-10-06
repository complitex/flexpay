package org.flexpay.common.process.filter;

import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.util.CollectionUtils;

import java.util.List;

/**
 * Implements process filtering by state
 */
public class ProcessStateFilter extends ObjectFilter {

	private Long selectedId;
	private List<ProcessStateObject> processStates = CollectionUtils.list();

	/**
	 * Default constructor. Creates {@link org.flexpay.common.process.filter.ProcessStateObject} objects represent all possible {@link org.flexpay.common.process.ProcessState} states
	 */
	public ProcessStateFilter() {

		processStates.add(ProcessStateObject.getCompletedState());
		processStates.add(ProcessStateObject.getRunningState());
		processStates.add(ProcessStateObject.getWaitingState());

		// TODO resolve how to deal with this state
		//processStates.add(ProcessStateObject.getCompletedWithErrorsState());
	}

	public ProcessState getProcessState() {

		return ProcessStateObject.getProcessState(selectedId);
	}

	public List<ProcessStateObject> getProcessStates() {
		return processStates;
	}

	public Long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}
}
