package org.flexpay.common.util.config;

import org.flexpay.common.test.SpringBeanAwareTestCase;

/**
 * Test is config loads OK
 */
public class TestCommonConfigLoader extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {
		testConfigLoader();
	}

	public void testConfigLoader() throws Exception {

		ApplicationConfig config = ApplicationConfig.getInstance();
		assertNotNull("Configuration load failed", config);

		assertNotNull("Test data root setup failed", config.getDataRoot());
		assertEquals("Test prop setup failed", "123", config.getTestProp());
	}
}
