package org.flexpay.ab.service;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegion;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;
import static org.junit.Assert.assertEquals;

public class TestTownService extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownService townService;

	private Date dt_08_08_08 = new GregorianCalendar(2008, 7, 8).getTime();
	private Date dt_08_09_08 = new GregorianCalendar(2008, 8, 8).getTime();

	@Test (expected = FlexPayExceptionContainer.class)
	public void testCreateEmptyTown() throws Exception {

		Town town = new Town();
		townService.create(town);
	}

	@Test (expected = FlexPayExceptionContainer.class)
	public void testCreateTownWithoutType() throws Exception {

		Town town = new Town();
		TownName townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("Тестовый город"));
		town.setNameForDate(townName, DateUtil.now());
		townService.create(town);
	}

	@Test (expected = FlexPayExceptionContainer.class)
	public void testCreateTownWithoutName() throws Exception {

		Town town = new Town();
		town.addTypeTemporal(new TownTypeTemporal(DateUtil.now(), new TownType(1L)));
		townService.create(town);
	}

	@SuppressWarnings ({"ConstantConditions"})
	@Test
	public void testCreateTown() throws Exception {

		Town town = new Town();
		town.setRegion(getDefaultRegion());

		// set town type
		town.setType(new TownType(1L));

		// set town name to 1 from 08/08/08
		TownName townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("1"));
		town.setNameForDate(townName, dt_08_08_08);

		// set town name to 2 from 08/09/08
		townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("2"));
		town.setNameForDate(townName, dt_08_09_08);

		// save and reread object
		townService.create(town);
		town = townService.readFull(stub(town));

		assertEquals("Invalid temporal name setup", "1", town.getNameForDate(dt_08_08_08).getDefaultNameTranslation());
		assertEquals("Invalid temporal name setup", "2", town.getNameForDate(dt_08_09_08).getDefaultNameTranslation());

		// set name to 3 for all period
		townName = new TownName();
		townName.addNameTranslation(new TownNameTranslation("3"));
		town.setNameForDates(townName, getPastInfinite(), getFutureInfinite());

		townService.update(town);

		// reread object from db
		town = townService.readFull(stub(town));

		assertEquals("Invalid temporal name setup", "3", town.getCurrentName().getDefaultNameTranslation());
		assertEquals("Invalid temporal name setup", "3", town.getNameForDate(dt_08_08_08).getDefaultNameTranslation());
		assertEquals("Invalid temporal name setup", "3", town.getNameForDate(dt_08_09_08).getDefaultNameTranslation());
	}
}
