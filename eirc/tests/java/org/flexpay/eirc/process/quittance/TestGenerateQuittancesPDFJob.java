package org.flexpay.eirc.process.quittance;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class TestGenerateQuittancesPDFJob extends EircSpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittancesPDFJasperJob job;

	// see init_db for ids definitions
	private Stub<EircServiceOrganization> organizationStub = new Stub<EircServiceOrganization>(2L);
	private Date dt_2007_12_01 = new GregorianCalendar(2007, 11, 1).getTime();
	private Date dt_2007_01_01 = new GregorianCalendar(2009, 0, 1).getTime();

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_SERVICE_ORGANIZATION_ID, organizationStub.getId());
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_FROM, dt_2007_12_01);
		contextVariables.put(GenerateQuittancesPDFJasperJob.PARAM_DATE_TILL, dt_2007_01_01);

		assertSame("Invalid result", Job.RESULT_NEXT, job.execute(contextVariables));

		assertNotNull("Output file was not specified", contextVariables.get(GenerateQuittancesPDFJasperJob.RESULT_FILE_ID));
	}

	@Test
	@Ignore
	public void testGenerateQuittancesProduction() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put("serviceOrganizationId", 5L);
		contextVariables.put("dateFrom", new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put("dateTill", new GregorianCalendar(2008, 10, 1).getTime());

		assertSame("Invalid result", Job.RESULT_NEXT, job.execute(contextVariables));

		assertNotNull("Output file was not specified", contextVariables.get(GenerateQuittancesPDFJasperJob.RESULT_FILE_ID));
	}
}
