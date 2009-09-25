package org.flexpay.payments.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.payments.service.statistics.AllTests.class,
		TestDocumentService.class,
		TestDocumentAdditionTypeService.class,
		TestDocumentStatusService.class,
		TestDocumentTypeService.class,
		TestOperationAdditionTypeService.class,
		TestOperationLevelService.class,
		TestOperationService.class,
		TestOperationStatusService.class,
		TestOperationTypeService.class,
		TestObjectsServices.class,
		TestServiceTypeService.class,
		TestSPService.class,
		TestUserPreferencesService.class
})
public class AllTests {

}
