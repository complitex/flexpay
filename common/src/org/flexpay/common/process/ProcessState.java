package org.flexpay.common.process;

public enum ProcessState {

    RUNING, WAITING, COMPLETED, COMPLETED_WITH_ERRORS;

	public boolean isCompleted() {
		return this == COMPLETED || this == COMPLETED_WITH_ERRORS;
	}

}
