package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.test.TransactionalSpringBeanAwareTestCase;
import org.flexpay.common.util.DateUtil;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

public class TestStreetService extends TransactionalSpringBeanAwareTestCase {

	@Autowired
	protected StreetDao streetDao;
	@Autowired
	protected StreetService streetService;
	@Autowired
	protected TownService townService;
	private StreetTypeService streetTypeService;

	@Autowired
	public void setService(@Qualifier ("streetTypeService") StreetTypeService service) {
		this.streetTypeService = service;
	}


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

		streetService.save(street);

		streetType = streetTypeService.read(2L);
		street.setTypeForDate(streetType, DateUtil.next(DateUtil.now()));
		System.out.println("Types: " + street.getTypesTimeLine());
		streetService.save(street);
	}

	@Test
	public void testGetStreetName() throws Throwable {
		Town town = townService.readFull(ApplicationConfig.getDefaultTownStub());
		assertNotNull("No default town", town);

		if (town.getStreets().isEmpty()) {
			System.err.println("No streets in default town!");
			return;
		}
		Street street = town.getStreets().iterator().next();
		streetService.format(stub(street), ApplicationConfig.getDefaultLocale(), true);
	}
}
