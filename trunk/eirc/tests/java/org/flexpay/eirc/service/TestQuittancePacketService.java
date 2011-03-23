package org.flexpay.eirc.service;

import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class TestQuittancePacketService extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittancePacketService service;

	/**
	 * Ensure {@link QuittancePacketService#suggestPacketNumber} works
	 */
	@Test
	public void testGetNextPacketNumber() {

		assertNotNull("nextPacketNumber call failed", service.suggestPacketNumber());
	}
}
