package org.flexpay.ab.action.region;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestRegionEditAction.class,
		TestRegionsListAction.class,
		TestRegionsListPageAction.class,
		TestRegionViewAction.class,
		TestRegionDeleteAction.class
})
public class AllTests {

}
