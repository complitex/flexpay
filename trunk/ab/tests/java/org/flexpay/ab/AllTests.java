package org.flexpay.ab;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

    public class AllTests extends TestCase {

        public AllTests(String name) {
            super(name);
        }

        public static Test suite() {
            TestSuite suite = new TestSuite();
            suite.addTest(org.flexpay.ab.persistence.AllTests.suite());
            return suite;
        }
    }


