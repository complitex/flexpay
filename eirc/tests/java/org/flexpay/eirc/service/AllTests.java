package org.flexpay.eirc.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		org.flexpay.eirc.service.importexport.AllTests.class,
		org.flexpay.eirc.service.exchange.AllTests.class,
		org.flexpay.eirc.service.registry.AllTests.class,
		TestBuildingService.class,
		TestEircAccountService.class,
		TestSpFileService.class,
		TestQuittanceService.class,
		TestQuittancePacketService.class,
		TestConsumerAttributeTypeService.class,
		TestConsumerAttributes.class
})
public class AllTests {

}
