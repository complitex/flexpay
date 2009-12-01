package org.flexpay.ab.action.building;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestBuildingCreateAction.class,
		TestBuildingsListAction.class,
		TestBuildingsListPageAction.class,
		TestBuildingViewAction.class,
		TestBuildingDeleteAction.class,
		TestBuildingAddressEditAction.class,
		TestBuildingAddressesListAction.class,
		TestBuildingAddressDeleteAction.class,
		TestBuildingAddressSetPrimaryStatusAction.class
})
public class AllTests {

}
