package org.flexpay.eirc.actions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestSpFileCreateAction.class,
		TestSpFileAction.class,
		TestPrintTicketAction.class,
		TestEircAccountListAction.class
})
public class AllTests {

}
