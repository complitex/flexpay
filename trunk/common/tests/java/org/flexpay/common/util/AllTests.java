package org.flexpay.common.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.util.config.AllTests.class,
		TestDateIntervalUtil.class,
		TestStringUtil.class,
		TestCRCUtil.class
		})
public class AllTests {

}
