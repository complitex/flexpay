package org.flexpay.payments.export.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestRegistryWriter.class,
        TestPaymentsRegistryDBGenerator.class
		})
public class AllTests {

}