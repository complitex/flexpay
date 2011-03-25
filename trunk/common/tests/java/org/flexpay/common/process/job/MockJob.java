package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;

import java.io.Serializable;
import java.util.Map;

public class MockJob extends Job {

    @Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		return Job.RESULT_NEXT;
	}

}
