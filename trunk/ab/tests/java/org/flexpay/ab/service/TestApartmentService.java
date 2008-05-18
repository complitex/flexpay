package org.flexpay.ab.service;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class TestApartmentService extends SpringBeanAwareTestCase {

	@Autowired
	protected ApartmentService apartmentService;
	@Autowired
	protected ApartmentDao apartmentDao;

	@Test
	public void testCreateApartment() throws Throwable {

		Apartment apartment = new Apartment();
		apartment.setBuilding(new Building(26L));

		ApartmentNumber number = new ApartmentNumber();
		number.setApartment(apartment);
		number.setValue("Test apartment number #24");
		number.setBegin(ApplicationConfig.getInstance().getPastInfinite());
		number.setEnd(ApplicationConfig.getInstance().getFutureInfinite());

		Set<ApartmentNumber> numbers = new HashSet<ApartmentNumber>();
		numbers.add(number);
		apartment.setApartmentNumbers(numbers);

		apartmentDao.create(apartment);

		assertNotNull("Apartment create failed", apartment.getId());
		assertNotNull("Apartment number create failed", number.getId());

		apartmentDao.delete(apartment);
	}

	@Test
	public void testFindApartment() throws Throwable {

		// See init_db script
		Apartment apartment = apartmentService.findApartmentStub(new Building(26L), "31");

		assertNotNull("Apartment find faild", apartment);
	}
}
