package org.flexpay.common.process;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ProcessStateComparator implements Comparator<Process> {
	
	private static Map<ProcessState, Integer> priorityMap;
	
	static {
		priorityMap = new HashMap<ProcessState, Integer>(4);
		priorityMap.put(ProcessState.RUNING, 10);
		priorityMap.put(ProcessState.WAITING, 20);
		priorityMap.put(ProcessState.COMPLITED, 30);
		priorityMap.put(ProcessState.COMPLITED_WITH_ERRORS, 40);
	}
	
	public int compare(Process obj1, Process obj2) {
		Integer obj1Priority = priorityMap.get(obj1.getProcessState());
		Integer obj2Priority = priorityMap.get(obj2.getProcessState());
		
		return obj1Priority.compareTo(obj2Priority);
	}

}
