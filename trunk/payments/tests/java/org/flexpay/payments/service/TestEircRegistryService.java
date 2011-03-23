package org.flexpay.payments.service;

import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNull;

public class TestEircRegistryService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private EircRegistryService eircRegistryService;

	@Test
	public void testGetRegistryByNumber() {

		assertNull("Returned registry must be null", eircRegistryService.getRegistryByNumber(999L, TestData.ORG_CN));
	}
}
