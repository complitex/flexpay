package org.flexpay.common.process;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.CollectionUtils.ar;

import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

public class ProcessStateComparator implements Comparator<Process>, Serializable {
	
	private static final Map<ProcessState, Integer> priorityMap = map(
			ar(ProcessState.RUNING, ProcessState.WAITING, ProcessState.COMPLITED, ProcessState.COMPLITED_WITH_ERRORS),
			ar(10, 20, 30, 40));
	
	public int compare(Process obj1, Process obj2) {
		Integer obj1Priority = priorityMap.get(obj1.getProcessState());
		Integer obj2Priority = priorityMap.get(obj2.getProcessState());
		
		return obj1Priority.compareTo(obj2Priority);
	}

}
