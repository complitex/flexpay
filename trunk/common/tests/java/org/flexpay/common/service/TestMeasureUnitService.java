package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestMeasureUnitService extends SpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitService measureUnitService;

	@Test
	public void testUpdateMeasureUnit() throws Throwable {

		MeasureUnit unit = measureUnitService.readFull(new Stub<MeasureUnit>(1L));
		assertNotNull("Measure unit not found", unit);

		unit.setName(new MeasureUnitName("Test"));
		measureUnitService.update(unit);
	}

	@Test
	public void testUpdateMeasureUnit2() throws Throwable {

		MeasureUnit unit = measureUnitService.readFull(new Stub<MeasureUnit>(1L));
		assertNotNull("Measure unit not found", unit);

		unit.setName(new MeasureUnitName(""));
		try {
			measureUnitService.update(unit);
		} catch (FlexPayExceptionContainer ex) {
			// expected
		}

		unit = measureUnitService.readFull(new Stub<MeasureUnit>(1L));
		assertNotNull("Failed reading just saved unit", unit);
		assertFalse("Unit names is empty", unit.getUnitNames().isEmpty());
		unit.setName(new MeasureUnitName("----------Test-------"));
		measureUnitService.update(unit);
	}
}
