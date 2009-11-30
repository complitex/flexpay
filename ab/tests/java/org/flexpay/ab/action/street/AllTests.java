package org.flexpay.ab.action.street;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestStreetEditAction.class,
		TestStreetsListAction.class,
		TestStreetsListPageAction.class,
		TestStreetViewAction.class,
		TestStreetDeleteAction.class
})
public class AllTests {

}
