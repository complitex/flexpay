package org.flexpay.eirc.util.config;

import org.flexpay.common.util.config.TestCommonConfigLoader;

/**
 * Test is config loads OK
 */
public class TestEircConfigLoader extends TestCommonConfigLoader {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {
		super.runTest();
		testEircConfigLoader();
	}

	public void testEircConfigLoader() throws Exception {

		ApplicationConfig config = ApplicationConfig.getInstance();
		assertNotNull("Default town setup failed", config.getDefaultTown());
		assertNotNull("Default region setup failed", config.getDefaultRegion());
		assertNotNull("Default country setup failed", config.getDefaultCountry());
	}
}
