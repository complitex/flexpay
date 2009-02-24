package org.flexpay.ab.service.history;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestTownTypeHistoryBuilder.class,
		TestTownHistoryBuilder.class // this test should go after town type builder, as depends on master correction
})
public class AllTests {

}