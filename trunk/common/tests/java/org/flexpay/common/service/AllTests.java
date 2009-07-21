package org.flexpay.common.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		LanguageServiceTest.class,
		TestMeasureUnitService.class,
		TestCurrencyInfoService.class,
//		,TestSendEmail.class
		TestUserPreferencesService.class
		})
public class AllTests {

}
