package org.flexpay.ab.service;

import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class TestDistrictService extends SpringBeanAwareTestCase {

	@Autowired
	private DistrictDao districtDao;

	@Test
	public void testCreateDistrict() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		District district = new District();
		district.setParent(town);

		DistrictName name = new DistrictName();
		name.setObject(district);

		DistrictNameTranslation translation = new DistrictNameTranslation();
		translation.setName("Test district");
		translation.setTranslatable(name);
		translation.setLang(ApplicationConfig.getDefaultLanguage());
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
