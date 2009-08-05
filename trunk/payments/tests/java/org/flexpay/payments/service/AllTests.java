package org.flexpay.payments.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.payments.service.statistics.AllTests.class,
		TestDocumentService.class,
		TestServiceDao.class,
		TestObjectsServices.class,
		TestOperationService.class
})
public class AllTests {

}
