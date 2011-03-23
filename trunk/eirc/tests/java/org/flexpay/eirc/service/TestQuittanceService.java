package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

public class TestQuittanceService extends EircSpringBeanAwareTestCase {

	@Autowired
	private QuittanceService quittanceService;
	@Autowired
	private QuittanceNumberService quittanceNumberService;

	private static final Stub<Quittance> quittanceStub = new Stub<Quittance>(1L);

	@Test
	public void testParseNumber() throws Exception {

		QuittanceNumberService.QuittanceNumberInfo info = quittanceNumberService.parseInfo("09002311489-10/2008-012");

		assertEquals("Invalid account number", "09002311489", info.getAccountNumber());
		// month is 0 based, so 10-th is actually a 9-th
		assertEquals("Invalid month", new GregorianCalendar(2008, 9, 1).getTime(), info.getMonth());
		assertEquals("Invalid number", 1, info.getNumber());
	}

	@Test
	public void testGetInvalidQuittanceByNumber() throws Exception {
		assertNull("Found invalid quittance", quittanceService.findByNumber("09002311489-10/2008-012"));
	}

	@Test (expected = FlexPayException.class)
	public void testGetInvalidQuittance() throws Exception {
		quittanceService.findByNumber("-----------------");
	}

	@Test
	public void testGetQuittanceByNumber() throws Exception {

		Quittance q = quittanceService.readFull(quittanceStub);
		assertNotNull("Not found quittance: " + quittanceStub, q);

		String number = quittanceNumberService.getNumber(q);
		Quittance qByNumber = quittanceService.findByNumber(number);
		assertNotNull("Not found valid quittance", qByNumber);

		log.debug("Quittance number: {}", number);

		assertEquals("Found quittance is not same", q, qByNumber);
	}

	@Test
	public void testShowAllQuittanceNumbers() {

		List<Quittance> quittances = Collections.emptyList();

		log.info("Valid quittances numbers are the following: ");
		for (Quittance q : quittances) {
			String quittanceNumber = quittanceNumberService.getNumber(q);

			log.info("{}", quittanceNumber);
		}
	}

}
