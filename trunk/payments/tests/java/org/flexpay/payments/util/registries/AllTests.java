package org.flexpay.payments.util.registries;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses ({
		TestEndOperationDayRegistryGenerator.class,
		TestExportBankPaymentsRegistry.class
})
public class AllTests {

}
