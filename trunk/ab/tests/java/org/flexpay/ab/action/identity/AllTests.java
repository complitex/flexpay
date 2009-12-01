package org.flexpay.ab.action.identity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestIdentityTypeEditAction.class,
		TestIdentityTypesListAction.class,
		TestIdentityTypeViewAction.class,
		TestIdentityTypeDeleteAction.class
})
public class AllTests {

}
