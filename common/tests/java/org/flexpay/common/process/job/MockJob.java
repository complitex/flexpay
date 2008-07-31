package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;

import java.util.HashMap;

public class MockJob extends Job{
    public String execute(HashMap parameters) throws FlexPayException {
        return Job.RESULT_NEXT;
    }
}
