package org.flexpay.common.service;

import static junit.framework.Assert.assertFalse;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LanguageServiceTest extends SpringBeanAwareTestCase {

	@Autowired
	protected LanguageService languageService;

	@Test
	public void testGetLanguages() {

		List<Language> languages = languageService.getLanguages();

		assertNotNull("No languages", languages);
		assertFalse("No languages defined", languages.isEmpty());
	}
}
