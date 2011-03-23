package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.flexpay.ab.util.TestUtils.createSimpleAddressAttributeType;
import static org.flexpay.ab.util.TestUtils.initNames;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.*;

public class TestAddressAttributeTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeEditAction action;
	@Autowired
	private AddressAttributeTypeDao attributeTypeDao;

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testEditNotSubmit() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("222212");
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", getLanguages().size(), action.getShortNames().size());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		attributeTypeDao.delete(attributeType);

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
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
	public void testIncorrectData1() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("321"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setShortNames(initNames("1221"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullAttributeType() throws Exception {

		action.setAttributeType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullAttributeTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectAttributeTypeId() throws Exception {

		action.setAttributeType(new AddressAttributeType(-10L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctAttributeType() throws Exception {

		action.setAttributeType(new AddressAttributeType(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDisabledAttributeType() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeType.disable();
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		attributeTypeDao.delete(attributeType);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("456"));
		action.setShortNames(initNames("789"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertNotNull("Invalid address attribute type id", action.getAttributeType().getId());

		attributeTypeDao.delete(action.getAttributeType());

	}

	@Test
	public void testEditSubmit() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setNames(initNames("456"));
		action.setShortNames(initNames("789"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertNotNull("Address attribute type must not be null", action.getAttributeType());

		System.out.println("Attribute translations: " + action.getAttributeType().getTranslations());

		String name = action.getAttributeType().getDefaultTranslation().getName();
		assertEquals("Invalid address attribute type name value", "456", name);
		String shortName = action.getAttributeType().getDefaultTranslation().getShortName();
		assertEquals("Invalid address attribute type short name value", "789", shortName);

		attributeTypeDao.delete(action.getAttributeType());

	}

}
