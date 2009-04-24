package org.flexpay.ab;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.ab.action.AllTests.class,
		org.flexpay.ab.persistence.AllTests.class,
		org.flexpay.ab.service.AllTests.class,
		org.flexpay.ab.sort.AllTests.class
})
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new JUnit4TestAdapter(AllTests.class));
		return suite;
	}
}
