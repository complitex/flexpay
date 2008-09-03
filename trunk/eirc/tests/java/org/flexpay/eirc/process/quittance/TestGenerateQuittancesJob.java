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

		contextVariables.put("dateFrom", new GregorianCalendar(2008, 5, 1).getTime());
		contextVariables.put("dateTill", DateUtil.now());

		job.execute(contextVariables);
	}
}
