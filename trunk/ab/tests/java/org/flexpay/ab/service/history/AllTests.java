package org.flexpay.ab.service.history;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestTownTypeHistoryBuilder.class,
		TestIdentityTypeHistoryBuilder.class,
		TestPersonHistoryBuilder.class,
		TestPersonsHistoryGenerator.class,
		TestTownHistoryBuilder.class, // this test should go after town type builder, as depends on master correction
		TestBuildingHistoryBuilder.class,
		TestApartmentHistoryBuilder.class,
		TestTownHistoryGenerator.class,
		TestPackTownHistory.class
})
public class AllTests {

}
