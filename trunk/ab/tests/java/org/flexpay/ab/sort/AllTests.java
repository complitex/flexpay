package org.flexpay.ab.sort;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestSortApartments.class,
		TestSortBuildings.class,
		TestSortStreets.class,
		TestSortDistricts.class,
		TestSortCountries.class,
		TestHQLSortCountries.class,
		TestHQLSortRegions.class,
		TestHQLSortTowns.class,
		TestHQLSortStreets.class,
		TestHQLSortDistricts.class,
		TestHQLSortApartments.class
})
public class AllTests {
}
