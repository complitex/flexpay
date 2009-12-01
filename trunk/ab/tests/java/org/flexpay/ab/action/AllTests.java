package org.flexpay.ab.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.ab.action.country.AllTests.class,
		org.flexpay.ab.action.region.AllTests.class,
		org.flexpay.ab.action.town.AllTests.class,
		org.flexpay.ab.action.district.AllTests.class,
		org.flexpay.ab.action.street.AllTests.class,
		org.flexpay.ab.action.building.AllTests.class,
		org.flexpay.ab.action.apartment.AllTests.class,
		org.flexpay.ab.action.identity.AllTests.class,
		org.flexpay.ab.action.measureunit.AllTests.class,
		org.flexpay.ab.action.person.AllTests.class
})
public class AllTests {

}
