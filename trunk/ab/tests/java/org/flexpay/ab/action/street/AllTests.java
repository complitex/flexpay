package org.flexpay.ab.action.street;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestStreetEditAction.class,
		TestStreetsListAction.class,
		TestStreetsListPageAction.class,
		TestStreetViewAction.class,
		TestStreetDeleteAction.class,
		TestStreetDistrictEditAction.class
/*
		TestStreetTypeEditAction.class,
		TestStreetTypesListAction.class,
		TestStreetTypeViewAction.class,
		TestStreetTypeDeleteAction.class
*/
})
public class AllTests {

}
