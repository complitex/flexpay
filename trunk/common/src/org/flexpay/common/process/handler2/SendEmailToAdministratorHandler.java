package org.flexpay.common.process.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.process.job.Job;

import java.util.Map;

public class SendEmailToAdministratorHandler extends TaskHandler {
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {
		//TODO send e-mail to administrator with handler parameters
		return Job.RESULT_NEXT;
	}
}
