package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.service.imp.EircAccountServiceImpl;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;

public class TestEircAccountService extends SpringBeanAwareTestCase {

	@Autowired
	protected EircAccountService eircAccountService;

	@Test
	@Ignore
	public void testGenerateAccountNumber() {
		String number = eircAccountService.nextPersonalAccount();
		System.out.println("Number: " + number);

		assertNotNull("Number generation failed", number);

		// prevent rollback
		number = eircAccountService.nextPersonalAccount();
		System.out.println("Number: " + number);

		assertNotNull("Number generation failed", number);
	}
}
