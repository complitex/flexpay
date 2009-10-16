package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLocale;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

		StreetType streetType = streetTypeService.read(TestData.STR_TYPE_VIADUKT);
		assertNotNull("No street type found", streetType);
		street.setType(streetType);
//		street.setTypeForDate(streetType, getPastInfinite());

		try {
			streetService.create(street);

			streetType = streetTypeService.read(new Stub<StreetType>(2L));
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
		} finally {
			if (street.isNotNew()) {
				streetService.delete(street);
			}
		}
	}

	@Test
	public void testGetStreetName() throws Throwable {
		Town town = townService.readFull(TestData.TOWN_NSK);
		assertNotNull("No default town", town);

		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(TestData.TOWN_NSK));
		List<Street> streets = streetService.find(filters);
		assertFalse("No streets in default town!", streets.isEmpty());

		Street street = streets.iterator().next();
		streetService.format(stub(street), getDefaultLocale(), true);
	}
}
