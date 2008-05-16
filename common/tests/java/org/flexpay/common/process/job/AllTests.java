package org.flexpay.common.process.job;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(TestJob.class));
		return suite;
	}
}