package org.flexpay.sz.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.sz.persistence.Oszn;
import org.flexpay.sz.persistence.corrections.DistrictCorrection;

public class TestCorrectionsService extends SpringBeanAwareTestCase {

	protected void runTest() throws Throwable {
		testGetDistrictCorrections();
	}

	public void testGetDistrictCorrections() throws Exception {
		CorrectionsService service =
				(CorrectionsService) applicationContext.getBean("correctionsService");

		Oszn oszn = new Oszn();
		oszn.setId(-5l);
		DistrictCorrection correction = service.findDistrictCorrection(oszn, "xbz");

		assertNull("Found invalid correction", correction);
	}
}
