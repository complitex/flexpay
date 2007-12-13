package org.flexpay.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.common.util.AllTests.suite());
		suite.addTest(org.flexpay.common.actions.AllTests.suite());
		return suite;
	}
}