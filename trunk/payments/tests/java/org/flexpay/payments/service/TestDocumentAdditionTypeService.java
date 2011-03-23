package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.DocumentAdditionType;
import org.flexpay.payments.persistence.DocumentAdditionTypeTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.DocumentAdditionType.CODE_ERC_ACCOUNT;
import static org.junit.Assert.*;

public class TestDocumentAdditionTypeService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DocumentAdditionTypeService documentAdditionTypeService;

	@Test
	public void testFindTypeByCode() {

		int validCodes[] = {CODE_ERC_ACCOUNT};
		for (int validCode : validCodes) {
			DocumentAdditionType documentAdditionType = null;

			try {
				documentAdditionType = documentAdditionTypeService.findTypeByCode(validCode);
			} catch (FlexPayException e) {
				fail("Reading document addition type by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined document status (code " + validCode + ")", documentAdditionType);
			assertEquals("Read status code " + documentAdditionType.getCode() + " is bad (request code was " + validCode + ")", validCode, documentAdditionType.getCode());

			DocumentAdditionTypeTranslation defaultTranslation = TranslationUtil.getTranslation(documentAdditionType.getTranslations(), ApplicationConfig.getDefaultLocale());
			assertNotNull("Status has no default translation", defaultTranslation);
		}
	}
}
