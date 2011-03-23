package org.flexpay.common.process;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestProcessLogger extends SpringBeanAwareTestCase {

	@Test
	public void testGetLogger() throws Exception {
		ProcessLogger.setThreadProcessId(-150L);
		Logger plog = ProcessLogger.getLogger(getClass());

		assertNotNull("Process logger retrival failed", plog);

		plog.info("Hello from test!");
		File logFile = ProcessLogger.getLogFile(-150L);
		assertTrue("Log file not found", logFile.exists());
	}
}
