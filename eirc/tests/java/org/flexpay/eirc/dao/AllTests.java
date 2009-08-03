package org.flexpay.eirc.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestServedBuildingDao.class,
		TestEircRegistryRecordPropertiesDao.class
})
public class AllTests {

}
