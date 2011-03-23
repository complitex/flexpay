package org.flexpay.tc.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestTariffCalculationResultService extends SpringBeanAwareTestCase {

	@Autowired
	private TariffCalculationResultService tariffCalculationResultService;

	@Test
	public void testUpdate() {

		TariffCalculationResult result = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(1L));

		result.setValue(BigDecimal.valueOf(2.1));
		tariffCalculationResultService.update(result);

		result = tariffCalculationResultService.read(new Stub<TariffCalculationResult>(1L));

		assertEquals("Object is not properly updated", result.getValue(), BigDecimal.valueOf(2.1));
	}

}
