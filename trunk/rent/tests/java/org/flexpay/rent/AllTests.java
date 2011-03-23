package org.flexpay.rent;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.rent.action.AllTests.class,
		org.flexpay.rent.persistence.AllTests.class,
		org.flexpay.rent.process.AllTests.class,
		org.flexpay.rent.service.AllTests.class
		})
public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(AllTests.class));
        return suite;
    }

}
