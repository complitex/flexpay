package org.flexpay.common.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.common.persistence.morphology.AllTests.class,
		org.flexpay.common.persistence.history.AllTests.class
})
public class AllTests {

}
