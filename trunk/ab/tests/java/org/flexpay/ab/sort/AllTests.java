package org.flexpay.ab.sort;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestHQLSortStreets.class,
		TestSortApartments.class,
		TestSortBuildings.class,
		TestSortStreets.class
})
public class AllTests {
}
