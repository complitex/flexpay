package org.flexpay.common.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.CurrencyInfo;
import org.flexpay.common.persistence.morphology.Gender;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCurrencyInfoService extends SpringBeanAwareTestCase {

	@Autowired
	private CurrencyInfoService service;

	@Test
	public void testGetDefaultCurrency() {

		CurrencyInfo info = service.getDefualtCurrency();

		assertEquals("Invalid gender", Gender.feminine, info.getGender());
	}
}
