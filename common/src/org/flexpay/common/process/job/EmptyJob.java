package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;

import java.io.Serializable;
import java.util.Map;

/**
 * Do Nothing in Job manager, just choose EMPTY_JOB_DEFAULT_TRANSITION context variable as job result
 */
public class EmptyJob extends Job{

	/**
	 * Default transition
	 */
	public final static String DEFAULT_TRANSITION = "EMPTY_JOB_DEFAULT_TRANSITION";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		String result = (String)parameters.get(DEFAULT_TRANSITION);
		if (result == null || result.length() ==0) {
			return Job.RESULT_NEXT;
		}else{
			return result;
		}
	}
}
