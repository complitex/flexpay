package org.flexpay.ab.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.ab.service.importexport.AllTests.class,
		org.flexpay.ab.service.history.AllTests.class,
		TestBuildingService.class,
		TestApartmentService.class,
		TestStreetTypeService.class,
		TestDistrictService.class,
		TestStreetService.class,
		TestPersonDao.class,
		TestI18nQuery.class,
		TestAddressService.class,
		TestTownService.class
//		, TestSecurity.class
})
public class AllTests {

}
