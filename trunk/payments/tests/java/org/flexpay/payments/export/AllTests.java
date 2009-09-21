package org.flexpay.payments.export;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.payments.export.util.AllTests.class,
		TestGeneratePaymentsRegistry.class,
		TestSignature.class
})
public class AllTests {

}
