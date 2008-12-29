package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.process.QuittanceNumberService;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.GregorianCalendar;

public class TestQuittanceService extends SpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;

	@Autowired
	private QuittanceNumberService quittanceNumberService;

	@Test
	public void testParseNumber() throws Exception {

		QuittanceNumberService.QuittanceNumberInfo info = quittanceNumberService.parseInfo("09002311489-10/2008-012");

		assertEquals("Invalid account number", "09002311489", info.getAccountNumber());
		// month is 0 based, so 10-th is actually a 9-th
		assertEquals("Invalid month", new GregorianCalendar(2008, 9, 1).getTime(), info.getMonth());
		assertEquals("Invalid number", 1, info.getNumber());
	}

	// todo test fetch quittance by number
}
