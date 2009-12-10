package org.flexpay.eirc.process;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.eirc.process.quittance.AllTests.class,
		org.flexpay.eirc.process.registry.AllTests.class
})
public class AllTests {

}
