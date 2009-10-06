package org.flexpay.common.process.job.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.job.Job;

import java.io.Serializable;
import java.util.Map;

/**
 * Sample job that does nothing
 */
public class JobA extends Job {

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		return Job.RESULT_NEXT;
	}
}
