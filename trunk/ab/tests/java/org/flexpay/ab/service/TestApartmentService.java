package org.flexpay.ab.service;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class TestApartmentService extends AbSpringBeanAwareTestCase {

	@Autowired
	protected ApartmentService apartmentService;
	@Autowired
	protected ApartmentDao apartmentDao;
	@Autowired
	private ObjectsFactory factory;

	@Test
	public void testCreateApartment() throws Throwable {

		Apartment apartment = factory.newApartment();
		apartment.setBuilding(new Building(26L));

		ApartmentNumber number = new ApartmentNumber();
		number.setApartment(apartment);
		number.setValue("Test apartment number #24");
		number.setBegin(ApplicationConfig.getPastInfinite());
		number.setEnd(ApplicationConfig.getFutureInfinite());

		Set<ApartmentNumber> numbers = new HashSet<ApartmentNumber>();
		numbers.add(number);
		apartment.setApartmentNumbers(numbers);

		apartmentService.create(apartment);

		assertNotNull("Apartment create failed", apartment.getId());
		assertNotNull("Apartment number create failed", number.getId());

		apartmentDao.delete(apartment);
	}

	@Test
	public void testFindApartment() throws Throwable {

		// See init_db script
		Stub<Apartment> apartment = apartmentService.findApartmentStub(new Building(1L), "31");

		assertNotNull("Apartment find faild", apartment);
	}
}
