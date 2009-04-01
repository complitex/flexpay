package org.flexpay.common.process;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.map;
import org.flexpay.common.persistence.sorter.ObjectSorter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

/**
 * ProcessStateComparator adds linear order on processes states 
 */
public class ProcessStateComparator implements Comparator<Process>, Serializable {

	private String order = ObjectSorter.ORDER_ASC;

	private static final Map<ProcessState, Integer> priorityMap = map(
			ar(ProcessState.RUNING, ProcessState.WAITING, ProcessState.COMPLITED, ProcessState.COMPLITED_WITH_ERRORS),
			ar(10, 20, 30, 40));
	
	public int compare(Process obj1, Process obj2) {
		Integer obj1Priority = priorityMap.get(obj1.getProcessState());
		Integer obj2Priority = priorityMap.get(obj2.getProcessState());

		int comparisonResult = obj1Priority.compareTo(obj2Priority);
		return (order.equals(ObjectSorter.ORDER_ASC)) ? comparisonResult : -comparisonResult;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
