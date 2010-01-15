package org.flexpay.payments.action.reports;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestAccReceivedPaymentsReportAction.class,
		TestAccReturnedPaymentsReportAction.class,
		TestDayReceivedPaymentsReportAction.class,
		TestDayReturnedPaymentsReportAction.class,
		TestImportPaymentsReportAction.class,
		TestPaymentOperationAction.class,
		TestPaymentOperationReportAction.class,
		TestReceivedPaymentsReportAction.class
})
public class AllTests {

}
