package org.flexpay.ab.service;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestDistrictService extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictService districtService;

	@Test (expected = FlexPayExceptionContainer.class)
	public void testSaveEmptyDistrict() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		District district = new District();
		district.setParent(town);

		districtService.create(district);
	}

	@Test
	public void testCreateDistrict() throws Throwable {

		Town town = ApplicationConfig.getDefaultTown();

		District district = new District();
		district.setParent(town);

		DistrictName name = new DistrictName();
		name.setObject(district);
		name.setTranslation(new DistrictNameTranslation("Test district"));

		district.setNameForDate(name, DateUtil.now());

		districtService.create(district);
	}

	@Test
	public void testUpdateDistrict() throws Throwable {

		District district = districtService.readFull(new Stub<District>(1L));
		assertNotNull("District not found", district);

		DistrictName name = new DistrictName();
		name.setTranslation(new DistrictNameTranslation("TEST_DISTRICT_UPDATE"));
		district.setNameForDate(name, DateUtil.parseBeginDate("2009/01/01"));
		districtService.update(district);

		DistrictName setName = district.getNameForDate(DateUtil.parseBeginDate("2009/01/01"));
		assertNotNull("Name setup failed", setName);
		assertEquals("Invalid name setup", "TEST_DISTRICT_UPDATE", setName.getDefaultNameTranslation());

		name = new DistrictName();
		name.setTranslation(new DistrictNameTranslation("TEST_DISTRICT_UPDATE_2"));
		district.setNameForDate(name, DateUtil.parseBeginDate("2009/01/01"));
		districtService.update(district);

		setName = district.getNameForDate(DateUtil.parseBeginDate("2009/01/01"));
		assertNotNull("Name setup failed", setName);
		assertEquals("Invalid name setup", "TEST_DISTRICT_UPDATE_2", setName.getDefaultNameTranslation());
	}
}
