package org.flexpay.eirc.process.quittance;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Map;

public class TestGenerateQuittancesJob extends SpringBeanAwareTestCase {

	@Autowired
	private GenerateQuittanceJob job;

	@Test
	public void testGenerateQuittances() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_FROM, new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put(GenerateQuittanceJob.PARAM_DATE_TILL, DateUtil.now());
		contextVariables.put(GenerateQuittanceJob.PARAM_SERVICE_ORGANISATION_ID, 1L);

		job.execute(contextVariables);
	}
}
