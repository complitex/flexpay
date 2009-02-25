package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestEircAccountService extends SpringBeanAwareTestCase {

	@Autowired
	protected EircAccountService eircAccountService;

	@Test
	public void testGenerateAccountNumber() {
	}
}
