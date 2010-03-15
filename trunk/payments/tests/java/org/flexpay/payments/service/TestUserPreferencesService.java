package org.flexpay.payments.service;

import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUserPreferencesService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private UserPreferencesService service;

	@Test
	public void testFindTestUser() throws Exception {

		PaymentsUserPreferences preferences = (PaymentsUserPreferences) service.loadUserByUsername("test");
		assertEquals("Invalid full name", "Тест Т. Тестов", preferences.getFullName());
		assertEquals("Invalid last name", "Тестов", preferences.getLastName());

		preferences.setLanguageCode("en".equals(preferences.getLanguageCode()) ? "ru" : "en");
		preferences.setPaymentCollectorId(1L);
		preferences.setPaymentPointId(null);
		service.saveFullData(preferences);

		UserPreferences updatedPreferences = service.loadUserByUsername("test");
		assertSame("Update failed", preferences.getLanguageCode(), updatedPreferences.getLanguageCode());
	}
}
