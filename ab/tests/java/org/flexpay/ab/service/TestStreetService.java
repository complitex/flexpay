package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.Set;

public class TestStreetService extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	@Override
	protected void runTest() throws Throwable {
		testCreateStreet();
	}

	public void testCreateStreet() throws Throwable {
		StreetDao streetDao =
				(StreetDao) applicationContext.getBean("streetDAO");

		Town town = new Town(1L);

		Street street = new Street();
		street.setParent(town);

		StreetName name = new StreetName();
		name.setObject(street);

		StreetNameTranslation translation = new StreetNameTranslation();
		translation.setName("Test street");
		translation.setTranslatable(name);
		translation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
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
}
