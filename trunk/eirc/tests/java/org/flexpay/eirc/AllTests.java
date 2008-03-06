package org.flexpay.eirc;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.eirc.service.importexport.AllTests.suite());
		suite.addTest(org.flexpay.eirc.util.config.AllTests.suite());
		return suite;
	}
}
