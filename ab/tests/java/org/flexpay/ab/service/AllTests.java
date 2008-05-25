package org.flexpay.ab.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//		org.flexpay.ab.service.importexport.AllTests.class,
		TestBuildingService.class,
//		TestApartmentService.class,
//		TestStreetTypeService.class,
//		TestDistrictService.class,
//		TestStreetService.class,
		TestPersonDao.class
		})
public class AllTests {

}
