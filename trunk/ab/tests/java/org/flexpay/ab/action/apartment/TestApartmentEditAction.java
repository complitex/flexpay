package org.flexpay.ab.action.apartment;

import org.flexpay.ab.actions.apartment.ApartmentEditAction;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentEditAction action;
	@Autowired
	private ApartmentDao apartmentDao;

	@Test
	public void testNullApartment() throws Exception {

		action.setApartment(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullApartmentId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setApartment(new Apartment(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setApartment(new Apartment(TestData.IVANOVA_27_330.getId()));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid building filter", TestData.ADDR_IVANOVA_27.getId(), action.getBuildingFilter());
		assertEquals("Invalid street filter", TestData.IVANOVA.getId(), action.getStreetFilter());
		assertEquals("Invalid town filter", TestData.TOWN_NSK.getId(), action.getTownFilter());
		assertEquals("Invalid region filter", TestData.REGION_NSK.getId(), action.getRegionFilter());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testIncorrectData2() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setBuildingFilter(-10L);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctApartment() throws Exception {

		action.setApartment(new Apartment(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledApartment() throws Exception {

		Apartment apartment = createSimpleApartment("222222");
		apartment.disable();

		apartmentDao.create(apartment);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		apartmentDao.delete(apartment);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setApartment(new Apartment(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentNumber("456");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid apartment id", action.getApartment().getId() > 0);

		apartmentDao.delete(action.getApartment());
	}

	@Test
	public void testEditSubmit() throws Exception {

		Apartment apartment = createSimpleApartment("987");

		apartmentDao.create(apartment);

		action.setApartment(apartment);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentNumber("789");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String number = action.getApartment().getNumber();
		assertEquals("Invalid apartment number value", "789", number);

		apartmentDao.delete(action.getApartment());
	}

}
