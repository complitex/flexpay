package org.flexpay.ab.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestCountry.class,
		TestPerson.class,
		TestNameTimeDependent.class,
		TestApartment.class
		})
public class AllTests {

}


