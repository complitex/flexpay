package org.flexpay.sz;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.flexpay.sz.service.TestCorrectionsService;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.sz.persistence.AllTests.class,
		org.flexpay.sz.service.AllTests.class
})
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new JUnit4TestAdapter(AllTests.class));
		return suite;
	}
}
