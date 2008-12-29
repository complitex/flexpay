package org.flexpay.common.process;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.common.process.job.AllTests.class,
		TestProcessManager.class
})
public class AllTests {

}