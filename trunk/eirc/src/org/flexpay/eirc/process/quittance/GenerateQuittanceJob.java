package org.flexpay.eirc.process.quittance;

import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.service.QuittanceService;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GenerateQuittanceJob extends Job {

	private QuittanceService quittanceService;

	public String execute(Map<Serializable, Serializable> contextVariables) {

		Date dateFrom = (Date) contextVariables.get("dateFrom");
		Date dateTill = (Date) contextVariables.get("dateTill");

		quittanceService.generateForServiceOrganisation(dateFrom, dateTill);

		return Job.RESULT_NEXT;
	}

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
