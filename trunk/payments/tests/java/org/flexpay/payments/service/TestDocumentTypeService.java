package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.DocumentType;
import org.flexpay.payments.persistence.DocumentTypeTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.DocumentType.*;
import static org.junit.Assert.*;

public class TestDocumentTypeService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DocumentTypeService documentTypeService;

	@Test
	public void testReadPredefinedTypes() {

		int validCodes[] = { CASH_PAYMENT, CASH_RETURN, CASHLESS_PAYMENT, CASHLESS_PAYMENT_RETURN };
		for (int validCode : validCodes) {
			DocumentType type = null;
			try {
				type = documentTypeService.read(validCode);
			} catch (FlexPayException e) {
				fail("Reading document type by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined document type (code " + validCode + ")", type);
			assertTrue("Read type code " + type.getCode() + " is bad (request code was " + validCode + ")", validCode == type.getCode());

			DocumentTypeTranslation defaultTranslation = TranslationUtil.getTranslation(type.getTranslations(), ApplicationConfig.getDefaultLocale());
			assertNotNull("Type has no default translation", defaultTranslation);
		}

		int invalidCodes[] = { -2, -1, 0, 5, 6, 7};
		for (int invalidCode : invalidCodes) {
			try {
				documentTypeService.read(invalidCode);
				fail("Successfully read type with invalid code " + invalidCode);
			} catch (FlexPayException e) {
			}
		}
	}

	@Test
	public void testDelete() {

		// attempt to delete type which doesn't exist mustn't cause any errors
		Long invalidId = 999L;
		documentTypeService.delete(new Stub<DocumentType>(invalidId));
	}	
}
