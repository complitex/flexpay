package org.flexpay.ab.action.filter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestCountryFilterAjaxAction.class,
		TestRegionFilterAjaxAction.class,
		TestTownFilterAjaxAction.class,
		TestDistrictFilterAjaxAction.class,
		TestStreetFilterAjaxAction.class,
		TestBuildingFilterAjaxAction.class,
		TestApartmentFilterAjaxAction.class
})
public class AllTests {

}
