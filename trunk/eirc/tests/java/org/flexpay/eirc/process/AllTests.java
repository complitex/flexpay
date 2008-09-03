package org.flexpay.eirc.process;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.flexpay.eirc.util.TestBigDecimal;
import org.flexpay.eirc.TestProcess;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.eirc.process.quittance.AllTests.class
        })
public class AllTests {

}
