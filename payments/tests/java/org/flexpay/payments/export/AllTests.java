package org.flexpay.payments.export;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.flexpay.payments.action.TestRecievedPaymentsReportAction;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.payments.export.util.AllTests.class
		})
public class AllTests {

}