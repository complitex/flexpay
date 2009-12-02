package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.AddressAttributeTypeEditAction;
import org.flexpay.ab.actions.buildings.BuildingAddressEditAction;
import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleAddressAttributeType;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAddressAttributeTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeEditAction action;
	@Autowired
	private AddressAttributeTypeDao attributeTypeDao;

	@Test
	public void testNullAttributeType() throws Exception {

		action.setAttributeType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullAttributeTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctAttributeType() throws Exception {

		action.setAttributeType(new AddressAttributeType(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledAttributeType() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeType.disable();
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		attributeTypeDao.delete(attributeType);

	}

	@Test
	public void testNullNamesAndShortNames() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());
		assertEquals("Invalid short names size for different languages", ApplicationConfig.getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("222212");
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		attributeTypeDao.delete(attributeType);

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("321"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setShortNames(initNames("1221"));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("456"));
		action.setShortNames(initNames("789"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertNotNull("Invalid address attribute type id", action.getAttributeType().getId());

		attributeTypeDao.delete(action.getAttributeType());

	}

	@Test
	public void testEditSubmit() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("456"));
		action.setShortNames(initNames("789"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertNotNull("Address attribute type must not be null", action.getAttributeType());

		String name = action.getAttributeType().getDefaultTranslation().getName();
		assertEquals("Invalid address attribute type name value", "456", name);
		String shortName = action.getAttributeType().getDefaultTranslation().getShortName();
		assertEquals("Invalid address attribute type short name value", "789", shortName);

		attributeTypeDao.delete(action.getAttributeType());

	}

}