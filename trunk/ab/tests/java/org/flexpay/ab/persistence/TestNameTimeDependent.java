package org.flexpay.ab.persistence;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class TestNameTimeDependent extends AbSpringBeanAwareTestCase {

	private Date dt_08_08_08 = new GregorianCalendar(2008, 7, 8).getTime();
	private Date dt_08_09_08 = new GregorianCalendar(2008, 8, 8).getTime();

	@Test
	public void testCreateTown() throws Exception {

		Town town = new Town();

		// set town name to 1 from 08/08/08
		TownName townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("1"));
		town.setNameForDate(townName, dt_08_08_08);

		// set town name to 2 from 08/09/08
		townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("2"));
		town.setNameForDate(townName, dt_08_09_08);

		assertEquals("Invalid temporal name setup", "1", town.getNameForDate(dt_08_08_08).getDefaultNameTranslation());
		assertEquals("Invalid temporal name setup", "2", town.getNameForDate(dt_08_09_08).getDefaultNameTranslation());
	}
}
