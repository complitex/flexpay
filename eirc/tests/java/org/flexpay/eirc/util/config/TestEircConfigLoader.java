package org.flexpay.eirc.util.config;

import static org.flexpay.ab.util.config.ApplicationConfig.*;
import org.flexpay.common.util.config.TestCommonConfigLoader;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

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
