package org.flexpay.ab.action.measureunit;

import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleMeasureUnit;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.dao.MeasureUnitDao;
import org.flexpay.common.persistence.MeasureUnit;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TestMeasureUnitEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private MeasureUnitEditAction action;
	@Autowired
	private MeasureUnitDao measureUnitDao;

	@Test
	public void testNullNames() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(TestData.MEASURE_UNIT_KBM));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		Map<Long, String> names = treeMap();
		names.put(564L, "test");

		action.setSubmitted("");
		action.setNames(names);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid names map size", getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullMeasureUnit() throws Exception {

		action.setMeasureUnit(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullMeasureUnitId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectMeasureUnitId() throws Exception {

		action.setMeasureUnit(new MeasureUnit(-10L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctMeasureUnit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditDisabledMeasureUnit() throws Exception {

		MeasureUnit measureUnit = createSimpleMeasureUnit("type2");
		measureUnit.disable();
		measureUnitDao.create(measureUnit);

		action.setMeasureUnit(measureUnit);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		measureUnitDao.delete(measureUnit);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setMeasureUnit(new MeasureUnit(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("555"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid measure unit id", action.getMeasureUnit().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		measureUnitDao.delete(action.getMeasureUnit());
	}

	@Test
	public void testEditSubmit() throws Exception {

		MeasureUnit measureUnit = createSimpleMeasureUnit("type1");
		measureUnitDao.create(measureUnit);

		action.setMeasureUnit(measureUnit);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("999"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		String name = action.getMeasureUnit().getDefaultTranslation().getName();
		assertEquals("Invalid measure unit name value", "999", name);

		measureUnitDao.delete(action.getMeasureUnit());
	}

}
