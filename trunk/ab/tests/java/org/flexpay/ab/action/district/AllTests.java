package org.flexpay.ab.action.district;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestDistrictEditAction.class,
		TestDistrictsListAction.class,
		TestDistrictsListPageAction.class,
		TestDistrictViewAction.class,
		TestDistrictDeleteAction.class
})
public class AllTests {

}
