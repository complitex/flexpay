package org.flexpay.eirc;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.flexpay.eirc.util.TestBigDecimal;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestProcess.class,
		org.flexpay.eirc.action.AllTests.class,
		org.flexpay.eirc.dao.AllTests.class,
		org.flexpay.eirc.service.AllTests.class,
		org.flexpay.eirc.util.config.AllTests.class,
		org.flexpay.eirc.sp.AllTests.class,
		org.flexpay.eirc.persistence.AllTests.class,
		org.flexpay.eirc.process.AllTests.class,
		TestBigDecimal.class
})
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new JUnit4TestAdapter(AllTests.class));
		return suite;
	}

}
