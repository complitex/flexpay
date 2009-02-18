package org.flexpay.tc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

public class TestTariffCalculationResultService extends SpringBeanAwareTestCase {

    @Autowired
    private TariffCalculationResultService tariffCalculationResultService;

    @Test
    public void testUpdate() {

        TariffCalculationResult result = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(1L));

        result.setValue(new BigDecimal(2));
        tariffCalculationResultService.update(result);

        result = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(1L));

        assertEquals("Object is not properly updated", result.getValue(), new BigDecimal(2));
    }

}
