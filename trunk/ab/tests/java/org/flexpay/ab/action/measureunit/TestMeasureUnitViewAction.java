package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.actions.measureunit.MeasureUnitViewAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleMeasureUnit;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.persistence.MeasureUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMeasureUnitViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitViewAction action;
	@Autowired
	private MeasureUnitDao measureUnitDao;

	@Test
	public void testAction() throws Exception {

		action.setMeasureUnit(new MeasureUnit(TestData.MEASURE_UNIT_KBM));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setMeasureUnit(new MeasureUnit(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullMeasureUnit() throws Exception {

		action.setMeasureUnit(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctMeasureUnit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(10902L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledMeasureUnit() throws Exception {

		MeasureUnit measureUnit = createSimpleMeasureUnit("3456");
		measureUnit.disable();
		measureUnitDao.create(measureUnit);

		action.setMeasureUnit(measureUnit);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		measureUnitDao.delete(measureUnit);

	}

}
