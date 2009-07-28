package org.flexpay.bti.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.bti.service.importexport.AllTests.class,
		org.flexpay.bti.service.history.AllTests.class,
		TestApartmentAttributeTypeService.class,
		TestBuildingAttributeTypeService.class,
		TestBuildingAttributeService.class,
		TestBuildingService.class,
		TestBtiBuildingService.class,
		TestApartmentAttribute.class,
		TestObjectsFactory.class
		})
public class AllTests {

}
