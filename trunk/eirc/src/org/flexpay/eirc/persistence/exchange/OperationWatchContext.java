package org.flexpay.eirc.persistence.exchange;

import org.apache.commons.lang.time.StopWatch;

import java.util.HashMap;
import java.util.Map;

public class OperationWatchContext {
	private Map<String, StopWatch> operationProcessWatch = new HashMap<String, StopWatch>();

	public void before(Operation op) {
		String key = getStringKey(op);
		StopWatch watch;
		if (!operationProcessWatch.containsKey(key)) {
			watch = new StopWatch();
			watch.start();
			operationProcessWatch.put(key, watch);
		} else {
			watch = operationProcessWatch.get(key);
			watch.	resume();
		}
	}

	public void after(Operation op) {
		String key = getStringKey(op);
		StopWatch watch = operationProcessWatch.get(key);
		watch.suspend();
	}

	private String getStringKey(Operation op) {
		return op.getClass().getName();
	}

	public Map<String, StopWatch> getOperationProcessWatch() {
		return operationProcessWatch;
	}
}
