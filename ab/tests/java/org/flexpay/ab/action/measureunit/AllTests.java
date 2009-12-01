package org.flexpay.ab.action.measureunit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestMeasureUnitEditAction.class,
		TestMeasureUnitsListAction.class,
		TestMeasureUnitViewAction.class,
		TestMeasureUnitDeleteAction.class
})
public class AllTests {

}
