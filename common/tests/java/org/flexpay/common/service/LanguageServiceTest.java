package org.flexpay.common.service;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class LanguageServiceTest extends SpringBeanAwareTestCase {

	private LanguageService languageService;

	@Autowired
	public void setLanguageService(@Qualifier ("languageService") LanguageService languageService) {
		this.languageService = languageService;
	}

	@Test
	public void testGetLanguages() {

		List<Language> languages = languageService.getLanguages();

		assertNotNull("No languages", languages);
		assertFalse("No languages defined", languages.isEmpty());
	}
}
