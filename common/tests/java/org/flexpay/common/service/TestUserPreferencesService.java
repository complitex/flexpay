package org.flexpay.common.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.UserPreferences;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TestUserPreferencesService extends SpringBeanAwareTestCase {

	@Autowired
	private UserPreferencesService service;

	@Test
	public void testFindTestUser() throws Exception {
		UserPreferences preferences = service.loadUserByUsername("test");
		assertEquals("Invalid full name", "Тест Т. Тестов", preferences.getFullName());
		assertEquals("Invalid last name", "Тестов", preferences.getLastName());

		preferences.setLanguageCode("en".equals(preferences.getLanguageCode()) ? "ru" : "en");
		service.saveFullData(preferences);

		UserPreferences updatedPreferences = service.loadUserByUsername("test");
		assertSame("Update failed", preferences.getLanguageCode(), updatedPreferences.getLanguageCode());
	}
}
