package org.flexpay.sz.persistence;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.junit.Test;

public class TestApplicationConfig extends SpringBeanAwareTestCase {

	@Test
	public void testGetDefaultCountry() {

		ApplicationConfig.getDefaultCountry();
	}
}
