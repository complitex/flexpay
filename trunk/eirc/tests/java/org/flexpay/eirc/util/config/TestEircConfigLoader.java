package org.flexpay.eirc.util.config;

import org.flexpay.common.util.config.TestCommonConfigLoader;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Test is config loads OK
 */
public class TestEircConfigLoader extends TestCommonConfigLoader {

	@Test
	public void testEircConfigLoader() throws Exception {

		ApplicationConfig config = ApplicationConfig.getInstance();
		assertNotNull("Default town setup failed", config.getDefaultTown());
		assertNotNull("Default region setup failed", config.getDefaultRegion());
		assertNotNull("Default country setup failed", config.getDefaultCountry());
	}
}
