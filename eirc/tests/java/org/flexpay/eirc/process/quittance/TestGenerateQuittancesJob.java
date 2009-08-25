package org.flexpay.eirc.process.quittance;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class TestGenerateQuittancesJob extends EircSpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittanceJob job;

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		Date from = new GregorianCalendar(2008, 5, 1).getTime();
		Date till = new GregorianCalendar(2008, 5, 30).getTime();
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, from);
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, till);
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANIZATION_ID, 1L);
		contextVariables.put(GenerateQuittanceJob.PARAM_TOWN_ID, 2L);

		job.execute(contextVariables);
	}
}
