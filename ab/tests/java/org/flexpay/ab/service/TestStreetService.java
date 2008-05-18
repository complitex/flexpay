package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TestStreetService extends SpringBeanAwareTestCase {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	protected StreetDao streetDao;

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

	@Test
	public void testRetriveStreetTypes() throws Throwable {

		Street street = streetDao.readFull(6L);

		if (street == null) {
			assertNotNull("Street retrival failed :(", street);
		}

		for (StreetTypeTemporal temporal : street.getTypeTemporals()) {
			log.info("testRetriveStreetTypes(): " + temporal);
		}
	}
}
