package org.flexpay.eirc.sp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestMbCorrectionsFileValidator.class,
		TestMbCorrectionsFileParser.class,
		TestMbCorrectionsFileParser.class,
		TestMbChargesFileParser.class,
		TestPaymentsRegistryProcessing.class
})
public class AllTests {

}
