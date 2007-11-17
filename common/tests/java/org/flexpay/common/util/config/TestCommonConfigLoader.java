package org.flexpay.common.util.config;

import org.junit.Test;

import java.net.URL;
import static junit.framework.Assert.assertNotNull;

/**
 * Test is config loads OK
 */
public class TestCommonConfigLoader {

	@Test
	public void testConfigLoader() throws Exception {

		URL url = getClass().getClassLoader().getResource(
				"org/flexpay/common/application_config.xml" );

		CommonConfigLoader configLoader = new CommonConfigLoader(url);
		configLoader.loadConfig();

		assertNotNull("Configuration load failed", ApplicationConfig.getInstance());
	}
}
