package org.flexpay.common.process;

public enum ProcessState {

    RUNING, WAITING, COMPLITED, COMPLITED_WITH_ERRORS;

	public boolean isCompleted() {
		return this == COMPLITED || this == COMPLITED_WITH_ERRORS;
	}

}
