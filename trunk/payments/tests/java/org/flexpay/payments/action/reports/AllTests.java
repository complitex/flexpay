package org.flexpay.payments.action.reports;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestPaymentOperationReportAction.class,
		TestReceivedPaymentsReportAction.class
})
public class AllTests {

}
