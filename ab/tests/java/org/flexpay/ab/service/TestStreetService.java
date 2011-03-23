package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLocale;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestStreetService extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetService streetService;
	@Autowired
	private TownService townService;
	@Autowired
	private StreetTypeService streetTypeService;

	@Test
	public void testCreateStreet() throws Throwable {

		Town town = getDefaultTown();

		Street street = new Street();
		street.setParent(town);

		StreetName name = new StreetName();
		name.setObject(street);

		StreetNameTranslation translation = new StreetNameTranslation("----Test street----");
		name.addNameTranslation(translation);
		street.setNameForDate(name, DateUtil.now());

		StreetType streetType = streetTypeService.readFull(TestData.STR_TYPE_VIADUKT);
		assertNotNull("No street type found", streetType);
		street.setType(streetType);
//		street.setTypeForDate(streetType, getPastInfinite());

		streetService.create(street);

		streetType = streetTypeService.readFull(new Stub<StreetType>(2L));
		street.setTypeForDate(streetType, DateUtil.next(DateUtil.now()));

		streetService.update(street);

		// TODO! fixme additional name insert creates additional temporal referenced to the same Name object,
		// TODO! so cascade delete will fail
		StreetName nameNew = new StreetName();
		nameNew.setObject(street);
		StreetNameTranslation translationNew = new StreetNameTranslation("----Test street new----");
		nameNew.addNameTranslation(translationNew);
		street.setNameForDate(nameNew, DateUtil.next(DateUtil.now()));

		streetService.update(street);
	}

	@Test
	public void testGetStreetName() throws Throwable {
		Town town = townService.readFull(TestData.TOWN_NSK);
		assertNotNull("No default town", town);

		ArrayStack filters = arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<Street> streets = streetService.find(filters);
		assertFalse("No streets in default town!", streets.isEmpty());

		Street street = streets.iterator().next();
		streetService.format(stub(street), getDefaultLocale(), true);
	}

	@Test
	public void testFindStreetByName() throws Throwable {

/*
		StopWatch watch = new StopWatch();
		watch.start();

		List<Street> streets = streetService.findByTownAndName(TestData.TOWN_HKV, "БАГРАТИОНА");

		watch.stop();
*/

//		System.out.println("Time spent finding street: " + watch);
	}

	@Test
	public void testFindStreetByNameAndType() throws Throwable {

		StopWatch watch = new StopWatch();
		watch.start();

		List<Street> streets = streetService.findByTownAndNameAndType(
				TestData.TOWN_HKV, "БАГРАТИОНА", TestData.STR_TYPE_STREET);

		watch.stop();

//		System.out.println("Time spent finding street: " + watch);
	}
}
