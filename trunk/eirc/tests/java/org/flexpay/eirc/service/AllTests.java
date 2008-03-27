package org.flexpay.eirc.service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.eirc.service.importexport.AllTests.suite());
		suite.addTest(org.flexpay.eirc.service.exchange.AllTests.suite());
		return suite;
	}
}
