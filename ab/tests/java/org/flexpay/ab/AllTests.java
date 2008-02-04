package org.flexpay.ab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(org.flexpay.ab.persistence.AllTests.suite());
        suite.addTest(org.flexpay.ab.service.AllTests.suite());
        return suite;
    }
}
