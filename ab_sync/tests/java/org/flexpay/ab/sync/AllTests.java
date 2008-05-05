package org.flexpay.ab.sync;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new SchedulerStopper());
		suite.addTest(new TestDumpHistoryJob());
		suite.addTest(new TestSyncAbJob());
		return suite;
	}
}
