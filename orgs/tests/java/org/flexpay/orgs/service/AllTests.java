package org.flexpay.orgs.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.orgs.service.history.AllTests.class,
		TestOrganisationService.class,
		TestInstanceService.class
})
public class AllTests {

}
