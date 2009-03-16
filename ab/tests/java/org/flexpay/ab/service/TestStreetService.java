package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.TransactionalSpringBeanAwareTestCase;
import org.flexpay.common.util.DateUtil;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

public class TestStreetService extends TransactionalSpringBeanAwareTestCase {

	private final Stub<Town> TOWN = new Stub<Town>(2L);

	@Autowired
	private StreetDao streetDao;
	@Autowired @Qualifier("streetService")
	private StreetService streetService;
	@Autowired
	private TownService townService;
	@Autowired @Qualifier("streetTypeService")
	private StreetTypeService streetTypeService;

	@Test
	@NotTransactional
	public void testCreateStreet() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		Street street = new Street();
		street.setParent(town);

		StreetName name = new StreetName();
		name.setObject(street);

		StreetNameTranslation translation = new StreetNameTranslation("----Test street----");
		name.addNameTranslation(translation);
		street.setNameForDate(name, DateUtil.now());

		StreetType streetType = streetTypeService.read(1L);
		assertNotNull("No street type found", streetType);
		street.setType(streetType);
//		street.setTypeForDate(streetType, ApplicationConfig.getPastInfinite());

		try {
			streetService.create(street);

			streetType = streetTypeService.read(2L);
			street.setTypeForDate(streetType, DateUtil.next(DateUtil.now()));

			streetService.update(street);

			// TODO! fixme additional name insert creates additional temporal referenced to the same Name object,
			// TODO! so cascade delete will fail
//			StreetName nameNew = new StreetName();
//			nameNew.setObject(street);
//			StreetNameTranslation translationNew = new StreetNameTranslation("----Test street new----");
//			nameNew.addNameTranslation(translationNew);
//			street.setNameForDate(nameNew, DateUtil.next(DateUtil.now()));
//
//			streetService.save(street);
		} finally {
			if (street.isNotNew()) {
				//noinspection ConstantConditions
				street = streetDao.read(street.getId());
				if (street != null) {
					streetDao.delete(street);
				}
			}
		}
	}

	@Test
	public void testGetStreetName() throws Throwable {
		Town town = townService.readFull(TOWN);
		assertNotNull("No default town", town);

		if (town.getStreets().isEmpty()) {
			System.err.println("No streets in default town!");
			return;
		}
		Street street = town.getStreets().iterator().next();
		streetService.format(stub(street), ApplicationConfig.getDefaultLocale(), true);
	}
}
