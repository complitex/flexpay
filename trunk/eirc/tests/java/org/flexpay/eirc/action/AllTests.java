package org.flexpay.eirc.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith (Suite.class)
@Suite.SuiteClasses ({
		TestSpFileCreateAction.class,
		TestSpFileAction.class,
		TestPrintTicketAction.class
})
public class AllTests {

}
