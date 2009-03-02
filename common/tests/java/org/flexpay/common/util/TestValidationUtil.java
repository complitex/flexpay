package org.flexpay.common.util;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestValidationUtil {

    @Test
    public void testCheckTariffCalculationResultValue() {
        // null & empty values should be rejected
        assertFalse("Null value must be rejected", ValidationUtil.checkTariffCalculationResultValue(null));
        assertFalse("Empty string must be rejected", ValidationUtil.checkTariffCalculationResultValue(""));

        // non-number string should be rejected
        assertFalse("Non-number string should be rejected", ValidationUtil.checkTariffCalculationResultValue("A word"));

        // number with no point should be accepted
        assertTrue("Number with no point should be accepted", ValidationUtil.checkTariffCalculationResultValue("23"));

        // number with colon instead of point should be accepted
        assertTrue("Number with colon instead of point should be accepted", ValidationUtil.checkTariffCalculationResultValue("12,34"));

        // number with less than five digits after decimal point should be accepted
        assertTrue("Number with 1 digit after decimal point should be accepted", ValidationUtil.checkTariffCalculationResultValue("1.2"));
        assertTrue("Number with 2 digits after decimal point should be accepted", ValidationUtil.checkTariffCalculationResultValue("1.23"));
        assertTrue("Number with 3 digits after decimal point should be accepted", ValidationUtil.checkTariffCalculationResultValue("1.234"));
        assertTrue("Number with 4 digits after decimal point should be accepted", ValidationUtil.checkTariffCalculationResultValue("1.2345"));

        // number with more than four digits after decimal point should be rejected
        assertFalse("Number with 5 digits after decimal point should be rejected", ValidationUtil.checkTariffCalculationResultValue("1.23456"));

        // negative numbers should be accepted
        assertTrue("Negative numbers should be accepted", ValidationUtil.checkTariffCalculationResultValue("-1"));
        assertTrue("Negative numbers should be accepted", ValidationUtil.checkTariffCalculationResultValue("-1.2"));
        assertTrue("Negative numbers should be accepted", ValidationUtil.checkTariffCalculationResultValue("-1,23"));
        assertTrue("Negative numbers should be accepted", ValidationUtil.checkTariffCalculationResultValue("-1.2345"));
    }
}
