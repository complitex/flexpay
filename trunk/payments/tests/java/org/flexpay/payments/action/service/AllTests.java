package org.flexpay.payments.action.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestServiceEditAction.class,
		TestServiceTypeEditAction.class,
		TestServiceTypeViewAction.class,
		TestServiceViewAction.class
})
public class AllTests {

}
