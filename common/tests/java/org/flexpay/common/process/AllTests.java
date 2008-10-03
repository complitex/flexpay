package org.flexpay.common.process;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;
import org.flexpay.common.locking.TestLockManager;
import org.flexpay.common.process.job.TestJob;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
//        suite.addTest(org.flexpay.common.process.job.AllTests.suite());
//        suite.addTest(new JUnit4TestAdapter(TestProcessManager.class));
		suite.addTest(new JUnit4TestAdapter(TestProcessLogger.class));
        return suite;
	}
}