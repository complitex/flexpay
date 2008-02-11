package org.flexpay.sz.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.sz.persistence.Oszn;

public class TestCorrectionsService extends SpringBeanAwareTestCase {

	protected void runTest() throws Throwable {
		testGetDistrictCorrections();
	}

	public void testGetDistrictCorrections() throws Exception {
		CorrectionsService service =
				(CorrectionsService) applicationContext.getBean("correctionsServiceAb");

		Oszn oszn = new Oszn();
		oszn.setId(-5l);

		try {
			service.findDistrictCorrection(oszn, "xbz");
			fail("Found invalid correction");

		}
		catch (FlexPayException e) {
			// good execution branch, everything is ok
		}
	}
}
