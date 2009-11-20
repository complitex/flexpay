package org.flexpay.ab.action.country;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestCountriesListAction.class,
		TestCountryCreateAction.class,
		TestCountryViewAction.class
})
public class AllTests {

}
