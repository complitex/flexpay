package org.flexpay.ab.persistence;

import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestApartment extends AbSpringBeanAwareTestCase {

	@Autowired
	private ObjectsFactory factory;

	@Test
	public void testSetupApartmentNumber() {

		Apartment apartment = Apartment.newInstance();
		apartment.setNumber("   ");
		assertNull("Blank number setup should erase value", apartment.getNumber());

		Date date = new GregorianCalendar(2009, Calendar.JANUARY, 1).getTime();
		apartment.setNumberForDate("123123", date);
		assertEquals("Invalid number setup", "123123", apartment.getNumberForDate(date));

		Date pastDate = new GregorianCalendar(1950, Calendar.JANUARY, 1).getTime();
		assertNull("Number setup for current date should not touch past intervals", apartment.getNumberForDate(pastDate));
	}
}
