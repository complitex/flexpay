package org.flexpay.eirc.process.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.QuittanceService;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class GenerateQuittanceJob extends Job {

	public static final String JOB_NAME = "generateQuittancesJob";

	public static final String PARAM_DATE_FROM = "dateFrom";
	public static final String PARAM_DATE_TILL = "dateTill";
	public static final String PARAM_SERVICE_ORGANISATION_ID = "serviceOrganisationId";

	private QuittanceService quittanceService;

	public String execute(Map<Serializable, Serializable> contextVariables) {

		Date dateFrom = (Date) contextVariables.get(PARAM_DATE_FROM);
		Date dateTill = (Date) contextVariables.get(PARAM_DATE_TILL);
		Long organisationId = (Long) contextVariables.get(PARAM_SERVICE_ORGANISATION_ID);

		Stub<ServiceOrganisation> stub = new Stub<ServiceOrganisation>(organisationId);
		quittanceService.generateForServiceOrganisation(stub, dateFrom, dateTill);

		return Job.RESULT_NEXT;
	}

	public static Set<String> getParameterNames() {
		return CollectionUtils.set(PARAM_DATE_FROM, PARAM_DATE_TILL, PARAM_SERVICE_ORGANISATION_ID);
	}

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
