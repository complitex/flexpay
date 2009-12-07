package org.flexpay.ab.action.person;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses ({
		TestPersonEditPageAction.class,
		TestPersonEditRegistrationFormAction.class,
		TestPersonSaveFIOAction.class,
		TestPersonSaveRegistrationAction.class,
		TestPersonsListAction.class,
		TestPersonViewAction.class,
		TestPersonViewIdentitiesAction.class,
		TestPersonDeleteAction.class
})
public class AllTests {

}
