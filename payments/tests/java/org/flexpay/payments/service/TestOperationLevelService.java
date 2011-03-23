package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.payments.persistence.OperationLevel;
import org.flexpay.payments.persistence.OperationLevelTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.OperationLevel.*;
import static org.junit.Assert.*;

public class TestOperationLevelService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OperationLevelService operationLevelService;

	@Test
	public void testRead() {
		int validCodes[] = { AVERAGE, HIGH, LOW, LOWEST, SUSPENDED };
		for (int validCode : validCodes) {
			OperationLevel level = null;
			try {
				level = operationLevelService.read(validCode);
			} catch (FlexPayException e) {
				fail("Reading operation level by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined operation level (code " + validCode + ")", level);
			assertTrue("Read level code " + level.getCode() + " is bad (expected code was " + validCode + ")", validCode == level.getCode());

			OperationLevelTranslation defaultTranslation = TranslationUtil.getTranslation(level.getTranslations());
			assertNotNull("Level has no default translation", defaultTranslation);
		}

		int invalidCodes[] = { -2, -1, 0, 6, 7, 8};
		for (int invalidCode : invalidCodes) {
			try {
				operationLevelService.read(invalidCode);
				fail("Successfully read operation level with invalid code " + invalidCode);
			} catch (FlexPayException e) {
			}
		}
	}
}
