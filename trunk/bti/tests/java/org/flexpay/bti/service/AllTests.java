package org.flexpay.bti.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.bti.service.importexport.AllTests.class,
		TestBuildingAttributeTypeService.class,
		TestBuildingService.class
		})
public class AllTests {

}