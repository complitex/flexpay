package org.flexpay.common.service;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.test.SpringBeanAwareTestCase;

import java.util.List;

public class LanguageServiceTest extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testGetLanguages();
	}

	public void testGetLanguages() {
		LanguageService languageService =
				(LanguageService) applicationContext.getBean("languageService");

		List<Language> languages = languageService.getLanguages();

		assertNotNull("No languages", languages);
		assertFalse("No languages defined", languages.isEmpty());
	}
}
