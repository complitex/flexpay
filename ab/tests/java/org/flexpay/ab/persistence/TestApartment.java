package org.flexpay.ab.persistence;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestApartment extends AbSpringBeanAwareTestCase {

	public void testSetupApartmentNumber() {

		Apartment apartment = new Apartment();
		apartment.setNumber("   ");
		assertNull("Blank number setup should erase value", apartment.getNumber());

		Date date = new GregorianCalendar(2009, Calendar.JANUARY, 1).getTime();
		apartment.setNumberForDate(date, "123123");
		assertEquals("Invalid number setup", "123123", apartment.getNumberForDate(date));

		Date pastDate = new GregorianCalendar(1950, Calendar.JANUARY, 1).getTime();
		assertNull("Number setup for current date should not touch past intervals", apartment.getNumberForDate(pastDate));
	}
}
