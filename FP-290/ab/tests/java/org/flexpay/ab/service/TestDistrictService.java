package org.flexpay.ab.service;

import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictService extends SpringBeanAwareTestCase {

	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private DistrictService districtService;

	@Test (expected = FlexPayExceptionContainer.class)
	public void testSaveEmptyDistrict() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		District district = new District();
		district.setParent(town);

		districtService.save(district);
	}

	@Test
	public void testCreateDistrict() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		District district = new District();
		district.setParent(town);

		DistrictName name = new DistrictName();
		name.setObject(district);

		DistrictNameTranslation translation = new DistrictNameTranslation("Test district");
		name.addNameTranslation(translation);

		district.setNameForDate(name, DateUtil.now());

		districtService.save(district);

		districtDao.delete(district);
	}
}
