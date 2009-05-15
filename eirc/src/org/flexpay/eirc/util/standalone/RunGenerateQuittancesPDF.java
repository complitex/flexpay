package org.flexpay.eirc.util.standalone;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.standalone.StandaloneTask;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.process.quittance.GenerateQuittancesPDFJasperJob;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class RunGenerateQuittancesPDF implements StandaloneTask {

	private GenerateQuittancesPDFJasperJob job;
	private Stub<EircServiceOrganization> organizationStub = new Stub<EircServiceOrganization>(2L);
	private Date dt_2007_12_01 = new GregorianCalendar(2007, 11, 1).getTime();
	private Date dt_2007_01_01 = new GregorianCalendar(2009, 0, 1).getTime();

	/**
	 * Execute task
	 */
	public void execute() {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_SERVICE_ORGANIZATION_ID, organizationStub.getId());
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_FROM, dt_2007_12_01);
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_TILL, dt_2007_01_01);

		try {
			job.execute(contextVariables);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Required
	public void setJob(GenerateQuittancesPDFJasperJob job) {
		this.job = job;
	}
}
