package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.actions.measureunit.MeasureUnitEditAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleMeasureUnit;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMeasureUnitEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitEditAction action;
	@Autowired
	private MeasureUnitDao measureUnitDao;

	@Test
	public void testNullMeasureUnit() throws Exception {

		action.setMeasureUnit(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullMeasureUnitId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(TestData.MEASURE_UNIT_KBM));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctMeasureUnit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledMeasureUnit() throws Exception {

		MeasureUnit measureUnit = createSimpleMeasureUnit("type2");
		measureUnit.disable();
		measureUnitDao.create(measureUnit);

		action.setMeasureUnit(measureUnit);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		measureUnitDao.delete(measureUnit);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("555"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid town type id", action.getMeasureUnit().getId() > 0);

		measureUnitDao.delete(action.getMeasureUnit());
	}

	@Test
	public void testEditSubmit() throws Exception {

		MeasureUnit town = createSimpleMeasureUnit("type1");
		measureUnitDao.create(town);

		action.setMeasureUnit(town);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("999"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getMeasureUnit().getDefaultTranslation().getName();
		assertEquals("Invalid town type name value", "999", name);

		measureUnitDao.delete(action.getMeasureUnit());
	}

}
