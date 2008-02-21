package org.flexpay.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.common.util.AllTests.suite());
		suite.addTest(org.flexpay.common.dao.AllTests.suite());
		suite.addTest(org.flexpay.common.actions.AllTests.suite());
		suite.addTest(org.flexpay.common.service.AllTests.suite());
        suite.addTest(org.flexpay.common.process.AllTests.suite());
        suite.addTest(org.flexpay.common.locking.AllTests.suite());
        return suite;
	}
}