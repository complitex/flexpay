package org.flexpay.sz.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.sz.persistence.Oszn;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.ExpectedException;

import static org.junit.Assert.fail;

public class TestCorrectionsService extends SpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("szCorrectionsService")
	private CorrectionsService service;

	@Test
	@ExpectedException (FlexPayException.class)
	public void testGetDistrictCorrections() throws Exception {

		Oszn oszn = new Oszn();
		oszn.setId(-5l);

		service.findDistrictCorrection(oszn, "xbz");
		fail("Found invalid correction");
	}

}
