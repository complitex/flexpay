package org.flexpay.orgs.service.history;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestOrganizationHistoryBuilder.class,
		TestBankHistoryBuilder.class,
		TestSubdivisionHistoryBuilder.class
})
public class AllTests {

}
