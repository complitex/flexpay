package org.flexpay.ab.action.apartment;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import static org.junit.Assert.*;

public class TestApartmentEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentEditAction action;
	@Autowired
	private ApartmentDao apartmentDao;
	@Autowired
	private BuildingDao buildingDao;

	@Test
	public void testNullApartment() throws Exception {

		action.setApartment(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setApartment(new Apartment(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setApartment(new Apartment(TestData.IVANOVA_27_330));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid building filter", TestData.ADDR_IVANOVA_27.getId(), action.getBuildingFilter());
		assertEquals("Invalid street filter", TestData.IVANOVA.getId(), action.getStreetFilter());
		assertEquals("Invalid town filter", TestData.TOWN_NSK.getId(), action.getTownFilter());
		assertEquals("Invalid region filter", TestData.REGION_NSK.getId(), action.getRegionFilter());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());

	}

	@Test
	public void testNullAddressId() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectAddressId1() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setBuildingFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectAddressId2() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setBuildingFilter(0L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctAddress() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setApartmentNumber("12333");
		action.setBuildingFilter(1212330L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledAddress() throws Exception {

		Building building = createSimpleBuilding("321");
		building.disable();
		for (BuildingAddress address : building.getBuildingses()) {
			address.disable();
		}
		buildingDao.create(building);

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setApartmentNumber("12333");
		action.setBuildingFilter(building.getDefaultBuildings().getId());
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

	@Test
	public void testNullApartmentId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectApartmentId() throws Exception {

		action.setApartment(new Apartment(-10L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctApartment() throws Exception {

		action.setApartment(new Apartment(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testEditDisabledApartment() throws Exception {

		Apartment apartment = createSimpleApartment("222222");
		apartment.disable();
		apartmentDao.create(apartment);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		apartmentDao.delete(apartment);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentNumber("456");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertTrue("Invalid apartment id", action.getApartment().getId() > 0);

		apartmentDao.delete(action.getApartment());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Apartment apartment = createSimpleApartment("987");
		apartmentDao.create(apartment);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentNumber("789");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		String number = action.getApartment().getNumber();
		assertEquals("Invalid apartment number value", "789", number);

		apartmentDao.delete(action.getApartment());
	}

}
