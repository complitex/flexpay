package org.flexpay.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.common.util.config.AllTests.suite());
		suite.addTest(new JUnit4TestAdapter(TestDateIntervalUtil.class));
		suite.addTest(new JUnit4TestAdapter(TestStringUtil.class));
		suite.addTest(new JUnit4TestAdapter(TestCRCUtil.class));
		return suite;
	}
}
