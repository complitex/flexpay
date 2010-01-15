package org.flexpay.payments.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.payments.action.reports.AllTests.class,
        org.flexpay.payments.action.monitor.AllTests.class,
		org.flexpay.payments.action.registry.AllTests.class,
		org.flexpay.payments.action.service.AllTests.class,
		org.flexpay.payments.action.quittance.AllTests.class,
		org.flexpay.payments.action.operations.AllTests.class,
		org.flexpay.payments.action.tradingday.AllTests.class
		})
public class AllTests {

}
