package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleMeasureUnit;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.persistence.MeasureUnit;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMeasureUnitDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitDeleteAction action;
	@Autowired
	private MeasureUnitDao measureUnitDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDeleteMeasureUnits() throws Exception {

		MeasureUnit measureUnit = createSimpleMeasureUnit("3456");
		measureUnitDao.create(measureUnit);

		action.setObjectIds(set(measureUnit.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		measureUnit = measureUnitDao.read(measureUnit.getId());
		assertTrue("Invalid status for town type. Must be disabled", measureUnit.isNotActive());

		measureUnitDao.delete(measureUnit);
	}

}
