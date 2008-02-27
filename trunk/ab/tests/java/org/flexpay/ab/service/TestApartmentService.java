package org.flexpay.ab.service;

import org.apache.log4j.Logger;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.Set;

public class TestApartmentService extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	@Override
	protected void runTest() throws Throwable {
		testCreateApartment();
		testFindApartment();
	}

	public void testCreateApartment() throws Throwable {
		ApartmentDao apartmentDao =
				(ApartmentDao) applicationContext.getBean("apartmentDao");

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

	public void testFindApartment() throws Throwable {

		ApartmentService apartmentService =
				(ApartmentService) applicationContext.getBean("apartmentService");

		// See init_db script
		Apartment apartment = apartmentService.findApartmentStub(new Building(26L), "31");

		Logger.getLogger("org.flexpay.ab.Test").error(apartment);

		assertNotNull("Apartment find faild", apartment);
	}
}
