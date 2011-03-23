package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestApartmentHistoryBuilder extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentHistoryBuilder historyBuilder;
	@Autowired
	private ApartmentService apartmentService;
	@Autowired
	private ObjectsFactory factory;

	@Test
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(factory.newApartment(), factory.newApartment());
		assertTrue("Diff of two empty apartment is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff3() {

		Diff diff = historyBuilder.diff(null, factory.newApartment());
		assertTrue("Diff of two empty apartments is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff4() {

		Stub<Apartment> stub = new Stub<Apartment>(3L);
		Apartment apartment = apartmentService.readFull(stub);
		if (apartment == null) {
			throw new IllegalStateException("No apartment " + stub + " found");
		}

		Diff diff = historyBuilder.diff(null, apartment);
		assertEquals("Invalid history builder", 2, diff.size());
	}

	@Test
	public void testPatch() {

		Stub<Apartment> stub = new Stub<Apartment>(3L);
		Apartment apartment = apartmentService.readFull(stub);
		if (apartment == null) {
			throw new IllegalStateException("No apartment " + stub + " found");
		}

		Diff diff = historyBuilder.diff(null, apartment);

		Apartment newApartment = factory.newApartment();
		historyBuilder.patch(newApartment, diff);

		assertEquals("Invalid building reference patch", apartment.getBuildingStub(), newApartment.getBuildingStub());

		for (ApartmentNumber number : apartment.getApartmentNumbers()) {
			assertEquals("Invalid patch of number for begin date " + DateUtil.format(number.getBegin()),
					number.getValue(), newApartment.getNumberForDate(number.getBegin()));
			assertEquals("Invalid patch of number for end date " + DateUtil.format(number.getEnd()),
					number.getValue(), newApartment.getNumberForDate(number.getEnd()));
		}
	}
}
