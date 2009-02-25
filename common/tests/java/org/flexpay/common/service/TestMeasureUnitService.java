package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestMeasureUnitService extends SpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("measureUnitService")
	private MeasureUnitService measureUnitService;

	@Test
	public void testUpdateMeasureUnit() throws Throwable {

		MeasureUnit unit = measureUnitService.read(new Stub<MeasureUnit>(1L));
		assertNotNull("Measure unit not found", unit);

		unit.setName(new MeasureUnitName("Test"));
		measureUnitService.save(unit);
	}

	@Test
	public void testUpdateMeasureUnit2() throws Throwable {

		MeasureUnit unit = measureUnitService.read(new Stub<MeasureUnit>(1L));
		assertNotNull("Measure unit not found", unit);

		unit.setName(new MeasureUnitName(""));
		try {
			measureUnitService.save(unit);
		} catch (FlexPayExceptionContainer ex) {
			// expected
		}

		unit = measureUnitService.read(new Stub<MeasureUnit>(1L));

		assertFalse("Unit names is empty", unit.getUnitNames().isEmpty());
		unit.setName(new MeasureUnitName("----------Test-------"));
		measureUnitService.save(unit);
	}
}
