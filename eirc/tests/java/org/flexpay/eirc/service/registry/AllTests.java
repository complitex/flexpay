package org.flexpay.eirc.service.registry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestRegistryRecordDaoExt());
		return suite;
	}
}