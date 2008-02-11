package org.flexpay.ab.service;

import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.Set;

public class TestDistrictService extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	@Override
	protected void runTest() throws Throwable {
		testCreateDistrict();
	}

	public void testCreateDistrict() throws Throwable {
		DistrictDao districtDao =
				(DistrictDao) applicationContext.getBean("districtDAO");

		Town town = new Town(1L);

		District district = new District();
		district.setParent(town);

		DistrictName name = new DistrictName();
		name.setObject(district);

		DistrictNameTranslation translation = new DistrictNameTranslation();
		translation.setName("Test district");
		translation.setTranslatable(name);
		translation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		Set<DistrictNameTranslation> translations = new HashSet<DistrictNameTranslation>();
		translations.add(translation);
		name.setTranslations(translations);

		DistrictNameTemporal temporal = new DistrictNameTemporal();
		temporal.setValue(name);
		temporal.setObject(district);
		TimeLine<DistrictName, DistrictNameTemporal> timeLine =
				new TimeLine<DistrictName, DistrictNameTemporal>(temporal);
		district.setNamesTimeLine(timeLine);

		districtDao.create(district);
		districtDao.delete(district);
	}
}
