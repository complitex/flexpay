package org.flexpay.ab.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.ab.action.building.AllTests.class,
		org.flexpay.ab.action.country.AllTests.class,
		org.flexpay.ab.action.person.AllTests.class,
		org.flexpay.ab.action.region.AllTests.class,
		org.flexpay.ab.action.town.AllTests.class
})
public class AllTests {

}
