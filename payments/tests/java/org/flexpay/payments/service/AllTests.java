package org.flexpay.payments.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestDocumentService.class,
		TestServiceDao.class
})
public class AllTests {

}
