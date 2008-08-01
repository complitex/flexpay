package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;

import java.util.Map;

public class MockJob extends Job {

	public String execute(Map parameters) throws FlexPayException {
		return Job.RESULT_NEXT;
	}
}
