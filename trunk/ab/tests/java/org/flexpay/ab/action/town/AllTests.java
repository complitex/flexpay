package org.flexpay.ab.action.town;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestTownEditAction.class,
		TestTownsListAction.class,
		TestTownsListPageAction.class,
		TestTownViewAction.class,
		TestTownDeleteAction.class,
		TestTownTypeEditAction.class,
		TestTownTypesListAction.class,
		TestTownTypeViewAction.class,
		TestTownTypeDeleteAction.class
})
public class AllTests {

}
