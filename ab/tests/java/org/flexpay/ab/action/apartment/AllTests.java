package org.flexpay.ab.action.apartment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestApartmentEditAction.class,
		TestApartmentsListAction.class,
		TestApartmentsListPageAction.class,
		TestApartmentDeleteAction.class,
		TestApartmentRegistrationAction.class
})
public class AllTests {

}
