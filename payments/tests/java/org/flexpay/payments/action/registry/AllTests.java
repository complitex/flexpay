package org.flexpay.payments.action.registry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses({
		TestRegistriesListAction.class,
		TestRegistryCommentaryEditAction.class,
		TestRegistryDeliveryHistoryAction.class,
		TestRegistryViewPageAction.class
		})
public class AllTests {

}
