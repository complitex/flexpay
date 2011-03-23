package org.flexpay.eirc.util.config;

import org.flexpay.common.util.config.TestCommonConfigLoader;
import org.junit.Test;

import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.junit.Assert.assertNotNull;

/**
 * Test is config loads OK
 */
public class TestEircConfigLoader extends TestCommonConfigLoader {

	@Test
	public void testEircConfigLoader() throws Exception {
		assertNotNull("Default town setup failed", getDefaultTown());
		assertNotNull("Default region setup failed", getDefaultRegion());
		assertNotNull("Default country setup failed", getDefaultCountry());
	}

}
