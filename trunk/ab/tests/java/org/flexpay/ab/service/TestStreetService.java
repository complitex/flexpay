package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.TimeLine;import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class TestStreetService extends SpringBeanAwareTestCase {

	@Autowired
	protected StreetDao streetDao;
	@Autowired
	protected StreetService streetService;
	@Autowired
	protected TownService townService;

	@Test
	public void testCreateStreet() throws Throwable {

		Town town = new Town(1L);

		Street street = new Street();
		street.setParent(town);

		StreetName name = new StreetName();
		name.setObject(street);

		StreetNameTranslation translation = new StreetNameTranslation();
		translation.setName("Test street");
		translation.setTranslatable(name);
		translation.setLang(ApplicationConfig.getDefaultLanguage());
		Set<StreetNameTranslation> translations = new HashSet<StreetNameTranslation>();
		translations.add(translation);
		name.setTranslations(translations);

		StreetNameTemporal temporal = new StreetNameTemporal();
		temporal.setValue(name);
		temporal.setObject(street);
		TimeLine<StreetName, StreetNameTemporal> timeLine =
				new TimeLine<StreetName, StreetNameTemporal>(temporal);
		street.setNamesTimeLine(timeLine);

		StreetType streetType = new StreetType(1L);
		StreetTypeTemporal typeTemporal = new StreetTypeTemporal();
		typeTemporal.setObject(street);
		typeTemporal.setValue(streetType);
		TimeLine<StreetType, StreetTypeTemporal> typeTimeLine =
				new TimeLine<StreetType, StreetTypeTemporal>(typeTemporal);
		street.setTypesTimeLine(typeTimeLine);

		streetDao.create(street);
		streetDao.delete(street);
	}

	@Test
	public void testGetStreetName() throws Throwable {
		Town town = townService.read(ApplicationConfig.getDefaultTown().getId());
		if (town.getStreets().isEmpty()) {
			System.out.println("No streets in default town!");
			return;
		}
		Street street = town.getStreets().iterator().next();
		streetService.format(stub(street), ApplicationConfig.getDefaultLocale(), true);
	}
}
