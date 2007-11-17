package org.flexpay.common.util;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.flexpay.common.TestCommon;


public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(org.flexpay.common.util.config.AllTests.suite());
		return suite;
	}
}