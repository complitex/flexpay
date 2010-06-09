package org.flexpay.common.process.filter;

import org.flexpay.common.process.ProcessState;
import org.jetbrains.annotations.NotNull;

/**
 * Process state wrapper for using in {@link org.flexpay.common.process.filter.ProcessStateFilter}
 * Provides predefined objects for all possible states
 */
public class ProcessStateObject {

	private Long id;
	private String name; // i18n code for display name

	/**
	 * Constructor 
	 * @param id object identifier
	 * @param name i18n code for display name
	 */
	private ProcessStateObject(@NotNull Long id, @NotNull String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Returns corresponding ProcessStateObject object by {@link org.flexpay.common.process.ProcessState}
	 * @param state process state value
	 * @return corresponding process state object
	 */
	public static ProcessStateObject getByProcessState(ProcessState state) {

		switch (state) {
			case COMPLETED:
				return completedState;
			case COMPLETED_WITH_ERRORS:
				return completedWithErrorsState;
			case RUNING:
				return runningState;
			case WAITING:
				return waitingState;
			default:
				return null;
		}
	}

	/**
	 * Returns {@link org.flexpay.common.process.ProcessState} which corresponds given state object
	 * @param processObjectId process object identifier
	 * @return state which corresponds given state object
	 */
	public static ProcessState getProcessState(Long processObjectId) {

		if (processObjectId == null) {
			return null;
		}

		switch (processObjectId.intValue()) {
			case 1:
				return ProcessState.COMPLETED;
			case 2:
				return ProcessState.COMPLETED_WITH_ERRORS;
			case 3:
				return ProcessState.WAITING;
			case 4:
				return ProcessState.RUNING;
			default:
				return null;
		}
	}

	// predefined states
	private static ProcessStateObject completedState =
			new ProcessStateObject(1L, "common.processing.process.state.completed");

	private static ProcessStateObject completedWithErrorsState =
			new ProcessStateObject(2L, "common.processing.process.state.completed_with_errors");

	private static ProcessStateObject waitingState =
			new ProcessStateObject(3L, "common.processing.process.state.waiting");

	private static ProcessStateObject runningState =
			new ProcessStateObject(4L, "common.processing.process.state.running");

	// predefined states getters
	public static ProcessStateObject getCompletedState() {
		return completedState;
	}

	public static ProcessStateObject getCompletedWithErrorsState() {
		return completedWithErrorsState;
	}

	public static ProcessStateObject getWaitingState() {
		return waitingState;
	}

	public static ProcessStateObject getRunningState() {
		return runningState;
	}

	// setters/getters
	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
