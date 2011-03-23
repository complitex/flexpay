package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.persistence.OperationStatusTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.OperationStatus.*;
import static org.junit.Assert.*;

public class TestOperationStatusService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OperationStatusService operationStatusService;

	@Test
	public void testRead() {
		int validCodes[] = {CREATED, REGISTERED, DELETED, RETURNED, ERROR, BLANK};
		for (int validCode : validCodes) {
			OperationStatus status = null;
			try {
				status = operationStatusService.read(validCode);
			} catch (FlexPayException e) {
				fail("Reading operation status by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined operation status (code " + validCode + ")", status);
			assertTrue("Read status code " + status.getCode() +
					   " is bad (expected code was " + validCode + ")", validCode == status.getCode());

			OperationStatusTranslation defaultTranslation = TranslationUtil.getTranslation(status.getTranslations());
			assertNotNull("Status has no default translation", defaultTranslation);
		}

		int invalidCodes[] = {-2, -1, 0, 7, 8, 9};
		for (int invalidCode : invalidCodes) {
			try {
				operationStatusService.read(invalidCode);
				fail("Successfully read operation status with invalid code " + invalidCode);
			} catch (FlexPayException e) {
			}
		}
	}
}
