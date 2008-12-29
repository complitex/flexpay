package org.flexpay.common.dao;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.JUnit4TestAdapter;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		org.flexpay.common.dao.paging.AllTests.class,
		TestCorrectionsDao.class,
		TestMeasureUnitDao.class
		})
public class AllTests {

}
