package org.flexpay.payments.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.payments.action.reports.AllTests.class,
        org.flexpay.payments.action.monitor.AllTests.class,
		org.flexpay.payments.action.registry.AllTests.class,
		TestConfirmationTradingDayServlet.class,
		TestRecievedPaymentsReportAction.class
		})
public class AllTests {

}
