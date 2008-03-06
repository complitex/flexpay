package org.flexpay.eirc.util.config;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestEircConfigLoader());
		return suite;
	}
}