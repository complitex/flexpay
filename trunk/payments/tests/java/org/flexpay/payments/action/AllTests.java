package org.flexpay.payments.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.payments.action.reports.AllTests.class,
		TestRecievedPaymentsReportAction.class
		})
public class AllTests {

}
