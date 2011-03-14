package org.flexpay.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.util.AllTests.class,
		org.flexpay.common.service.AllTests.class,
		org.flexpay.common.dao.AllTests.class,
		org.flexpay.common.drools.AllTests.class,
		org.flexpay.common.locking.AllTests.class,
		org.flexpay.common.action.AllTests.class,
		org.flexpay.common.reporting.AllTests.class,
		org.flexpay.common.process.AllTests.class,
		org.flexpay.common.persistence.AllTests.class
		})
public class AllTests {

}
