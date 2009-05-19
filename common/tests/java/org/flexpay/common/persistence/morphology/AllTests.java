package org.flexpay.common.persistence.morphology;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.flexpay.common.locking.TestLockManager;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.persistence.morphology.number.AllTests.class
		})
public class AllTests {

}
