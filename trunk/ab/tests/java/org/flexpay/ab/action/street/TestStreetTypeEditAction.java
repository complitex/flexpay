package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.flexpay.ab.util.TestUtils.createSimpleStreetType;
import static org.flexpay.ab.util.TestUtils.initNames;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.*;

public class TestStreetTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetTypeEditAction action;
	@Autowired
	private StreetTypeDao streetTypeDao;

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setStreetType(new StreetType(0L));
		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setStreetType(new StreetType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setStreetType(new StreetType(TestData.STR_TYPE_STREET));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		Map<Long, String> names = treeMap();
		names.put(564L, "test");
		Map<Long, String> shortNames = treeMap();
		shortNames.put(2L, "shorttest");

		action.setSubmitted("");
		action.setNames(names);
		action.setShortNames(shortNames);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid names map size", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid shortNames map size", getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testNullShortNames1() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("123"));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street type id", action.getStreetType().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetTypeDao.delete(action.getStreetType());
	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setShortNames(initNames("345"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullStreetType() throws Exception {

		action.setStreetType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullStreetTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectStreetTypeId() throws Exception {

		action.setStreetType(new StreetType(-10L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctStreetType() throws Exception {

		action.setStreetType(new StreetType(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledStreetType() throws Exception {

		StreetType streetType = createSimpleStreetType("type2");
		streetType.disable();
		streetTypeDao.create(streetType);

		action.setStreetType(streetType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetTypeDao.delete(streetType);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setStreetType(new StreetType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("555"));
		action.setShortNames(initNames("666"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid street type id", action.getStreetType().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetTypeDao.delete(action.getStreetType());
	}

	@Test
	public void testEditSubmit() throws Exception {

		StreetType street = createSimpleStreetType("type1");
		streetTypeDao.create(street);

		action.setStreetType(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("999"));
		action.setShortNames(initNames("000"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		String name = action.getStreetType().getDefaultTranslation().getName();
		assertEquals("Invalid street type name value", "999", name);
		String shortName = action.getStreetType().getDefaultTranslation().getShortName();
		assertEquals("Invalid street type short name value", "000", shortName);

		streetTypeDao.delete(action.getStreetType());
	}

}