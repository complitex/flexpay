package org.flexpay.eirc.process.quittance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.eirc.process.quittance.report.AllTests.class,
		TestGenerateQuittancesJob.class,
		TestGenerateQuittancesPDFJob.class,
		TestOpenPattern.class,
		TestRunDuplicateQuittanceJobs.class
})
public class AllTests {
}
