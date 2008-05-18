package org.flexpay.common.util.config;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Test is config loads OK
 */
public class TestCommonConfigLoader extends SpringBeanAwareTestCase {


	@Test
	public void testConfigLoader() throws Exception {

		ApplicationConfig config = ApplicationConfig.getInstance();
		assertNotNull("Configuration load failed", config);

		assertNotNull("Test data root setup failed", config.getDataRoot());
		assertEquals("Test prop setup failed", "123", config.getTestProp());
	}
}
