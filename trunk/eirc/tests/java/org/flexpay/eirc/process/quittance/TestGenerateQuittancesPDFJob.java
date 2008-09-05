package org.flexpay.eirc.process.quittance;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.process.job.Job;
import org.junit.Test;
import static org.junit.Assert.assertNotSame;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Map;

public class TestGenerateQuittancesPDFJob extends SpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittancePDFJob job;

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put("serviceOrganisationId", 95L);
		contextVariables.put("dateFrom", new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put("dateTill", DateUtil.now());

		assertNotSame("Invalid result", Job.RESULT_ERROR, job.execute(contextVariables));
	}
}
