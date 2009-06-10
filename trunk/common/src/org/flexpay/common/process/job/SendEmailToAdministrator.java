package org.flexpay.common.process.job;

import org.flexpay.common.exception.FlexPayException;

import java.io.Serializable;
import java.util.Map;

public class SendEmailToAdministrator extends Job{
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		//TODO send e-mail to administrator with job parameters
		return Job.RESULT_NEXT;
	}
}
