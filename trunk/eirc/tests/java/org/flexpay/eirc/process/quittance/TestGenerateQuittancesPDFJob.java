package org.flexpay.eirc.process.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class TestGenerateQuittancesPDFJob extends SpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittancesPDFJasperJob job;

	// see init_db for ids definitions
	private Stub<ServiceOrganisation> organisationStub = new Stub<ServiceOrganisation>(1L);
	private Date dt_2007_12_01 = new GregorianCalendar(2007, 11, 1).getTime();
	private Date dt_2007_01_01 = new GregorianCalendar(2008, 0, 1).getTime();

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_SERVICE_ORGANISATION_ID, organisationStub.getId());
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_FROM, dt_2007_12_01);
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_TILL, dt_2007_01_01);

		assertSame("Invalid result", Job.RESULT_NEXT, job.execute(contextVariables));

		assertNotNull("Output file was not specified", contextVariables.get(GenerateQuittancesPDFJasperJob.RESULT_FILE_NAME));
	}

	@Test
	@Ignore
	public void testGenerateQuittancesProduction() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put("serviceOrganisationId", 5L);
		contextVariables.put("dateFrom", new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put("dateTill", new GregorianCalendar(2008, 10, 1).getTime());

		assertSame("Invalid result", Job.RESULT_NEXT, job.execute(contextVariables));

		assertNotNull("Output file was not specified", contextVariables.get(GenerateQuittancesPDFJasperJob.RESULT_FILE_NAME));
	}
}
