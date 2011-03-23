package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.persistence.OperationTypeTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.payments.persistence.OperationType.*;
import static org.junit.Assert.*;

public class TestOperationTypeService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OperationTypeService operationTypeService;

	@Test
	public void testRead() {
		int validCodes[] = {SERVICE_CASH_PAYMENT, SERVICE_CASH_RETURN, SERVICE_CASHLESS_PAYMENT, SERVICE_CASHLESS_RETURN,
							QUITTANCE_CASH_PAYMENT, QUITTANCE_CASH_RETURN, QUITTANCE_CASHLESS_PAYMENT, QUITTANCE_CASHLESS_RETURN};

		for (int validCode : validCodes) {
			OperationType type = null;
			try {
				type = operationTypeService.read(validCode);
			} catch (FlexPayException e) {
				fail("Reading operation type by code failed: " + e.getMessage());
			}

			assertNotNull("Failed reading by code predefined operation type (code " + validCode + ")", type);
			assertTrue("Read type code " + type.getCode() +
					   " is bad (expected code was " + validCode + ")", validCode == type.getCode());

			OperationTypeTranslation defaultTranslation = TranslationUtil.getTranslation(type.getTranslations());
			assertNotNull("Type has no default translation", defaultTranslation);
		}

		int invalidCodes[] = {-2, -1, 0, 9, 10, 11};
		for (int invalidCode : invalidCodes) {
			try {
				operationTypeService.read(invalidCode);
				fail("Successfully read operation type with invalid code " + invalidCode);
			} catch (FlexPayException e) {
			}
		}
	}
}
