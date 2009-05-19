package org.flexpay.common.persistence.morphology;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.common.persistence.morphology.number.AllTests.class,
		org.flexpay.common.persistence.morphology.currency.AllTests.class
})
public class AllTests {

}
