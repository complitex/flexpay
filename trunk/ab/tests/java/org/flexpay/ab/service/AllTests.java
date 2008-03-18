package org.flexpay.ab.service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.ab.service.importexport.AllTests.suite());
		suite.addTest(new TestStreetTypeService());
		suite.addTest(new TestDistrictService());
		suite.addTest(new TestStreetService());
		suite.addTest(new TestBuildingService());
		suite.addTest(new TestApartmentService());
		suite.addTest(new TestPersonDao());
		return suite;
    }
}
