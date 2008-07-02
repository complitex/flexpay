package org.flexpay.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.util.AllTests.class,
		org.flexpay.common.service.AllTests.class,
		org.flexpay.common.dao.AllTests.class,
		org.flexpay.common.locking.AllTests.class,
		org.flexpay.common.actions.AllTests.class,
		org.flexpay.common.reporting.AllTests.class
		})
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new AllTests());
        suite.addTest(org.flexpay.common.process.AllTests.suite());
        suite.addTest(new JUnit4TestAdapter(AllTests.class));
        return suite;
	}

	public void runTest() throws Throwable {
		String path = "org/flexpay/common/test.txt";
		assertNotNull("Test resource search failed", getClass().getClassLoader().getResource(path));
	}
}
