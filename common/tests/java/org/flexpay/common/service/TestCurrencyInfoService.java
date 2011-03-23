package org.flexpay.common.service;

import org.flexpay.common.persistence.CurrencyInfo;
import org.flexpay.common.persistence.morphology.Gender;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TestCurrencyInfoService extends SpringBeanAwareTestCase {

	@Autowired
	private CurrencyInfoService service;

	@Test
	public void testGetDefaultCurrency() {

		CurrencyInfo info = service.getDefaultCurrency();

		assertEquals("Invalid gender", Gender.feminine, info.getGender());
	}
}
