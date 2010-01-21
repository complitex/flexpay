package org.flexpay.eirc.sp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestMbCorrectionsFileValidator.class,
        TestMbChargesFileValidator.class,
		TestMbCorrectionsFileParser.class,
		TestMbCorrectionsFileParser2.class,
		TestMbChargesFileParser.class,
		TestMbChargesFileParser2.class,
		TestPaymentsRegistryProcessing.class
})
public class AllTests {

}
