package org.flexpay.eirc.process.quittance;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.service.QuittanceService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class GenerateQuittanceJob extends Job {

	public static final String JOB_NAME = "generateQuittancesJob";

	public static final String PARAM_DATE_FROM = "dateFrom";
	public static final String PARAM_DATE_TILL = "dateTill";
	public static final String PARAM_SERVICE_ORGANIZATION_ID = "serviceOrganizationId";
	public static final String PARAM_TOWN_ID = "townId";

	private QuittanceService quittanceService;

	public String execute(Map<Serializable, Serializable> contextVariables) throws FlexPayException {

		Date dateFrom = (Date) contextVariables.get(PARAM_DATE_FROM);
		Date dateTill = (Date) contextVariables.get(PARAM_DATE_TILL);
		if (dateFrom == null || dateTill == null) {
			throw new FlexPayException("No date", "eirc.error.quittance.job.no_dates");
		}
		if (!DateUtil.truncateMonth(dateFrom).equals(DateUtil.truncateMonth(dateTill))) {
			throw new FlexPayException("Invalid date", "eirc.error.quittance.job.month_only_allowed");
		}
		Long organizationId = (Long) contextVariables.get(PARAM_SERVICE_ORGANIZATION_ID);
		Long townId = (Long) contextVariables.get(PARAM_TOWN_ID);

		Stub<EircServiceOrganization> organizationStub = new Stub<EircServiceOrganization>(organizationId);
		Stub<Town> townStub = new Stub<Town>(townId);
		quittanceService.generateForServiceOrganization(organizationStub, townStub, dateFrom, dateTill);

		return Job.RESULT_NEXT;
	}

	public static Set<String> getParameterNames() {
		return CollectionUtils.set(PARAM_DATE_FROM, PARAM_DATE_TILL, PARAM_SERVICE_ORGANIZATION_ID);
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}