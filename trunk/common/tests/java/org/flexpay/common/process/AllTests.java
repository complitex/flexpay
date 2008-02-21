package org.flexpay.common.process;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
//		suite.addTest(new TestProcessManager());

		return suite;
	}
}