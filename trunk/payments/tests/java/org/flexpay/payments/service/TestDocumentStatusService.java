package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.persistence.DocumentStatusTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.DocumentStatus.*;
import static org.junit.Assert.*;

public class TestDocumentStatusService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DocumentStatusService documentStatusService;

	@Test
	public void testRead() {

		int validCodes[] = {CREATED, DELETED, ERROR, REGISTERED, RETURNED};
		for (int validCode : validCodes) {
			DocumentStatus status = null;
			try {
				status = documentStatusService.read(validCode);
			} catch (FlexPayException e) {
				fail("Reading document status by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined document status (code " + validCode + ")", status);
			assertTrue("Read status code " + status.getCode() + " is bad (request code was " + validCode + ")", validCode == status.getCode());

			DocumentStatusTranslation defaultTranslation = TranslationUtil.getTranslation(status.getTranslations());
			assertNotNull("Status has no default translation", defaultTranslation);
		}

		int invalidCodes[] = {-2, -1, 0, 6, 7, 8};
		for (int invalidCode : invalidCodes) {
			try {
				documentStatusService.read(invalidCode);
				fail("Successfully read status with invalid code " + invalidCode);
			} catch (FlexPayException e) {
			}
		}
	}
}
