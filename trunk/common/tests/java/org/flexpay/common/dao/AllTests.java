package org.flexpay.common.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.dao.paging.AllTests.class,
		TestCorrectionsDao.class,
		TestMeasureUnitDao.class,
		TestCountOptimize.class
		})
public class AllTests {

}
