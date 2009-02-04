package org.flexpay.bti.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.bti.service.importexport.AllTests.class,
		TestBuildingAttributeTypeService.class,
		TestBuildingAttributeService.class,
		TestBuildingService.class,
		TestBtiBuildingService.class
		})
public class AllTests {

}