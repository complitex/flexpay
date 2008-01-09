package org.flexpay.common.util.config;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;
import java.net.URL;

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

		ServletContext context = new MockServletContext();
		configLoader.setServletContext(context);
		configLoader.setApplicationProperties();

		System.out.println("Languages: " + context.getAttribute("languages"));
	}
}
