package org.flexpay.ab.action.apartment;

import org.flexpay.ab.actions.apartment.ApartmentDeleteAction;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleApartment;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentDeleteAction action;
	@Autowired
	private ApartmentDao apartmentDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteApartments() throws Exception {

		Apartment apartment = createSimpleApartment("1234");

		apartmentDao.create(apartment);

		action.setObjectIds(set(apartment.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		apartment = apartmentDao.read(apartment.getId());
		assertTrue("Invalid status for apartment. Must be disabled", apartment.isNotActive());

		apartmentDao.delete(apartment);

	}

}
