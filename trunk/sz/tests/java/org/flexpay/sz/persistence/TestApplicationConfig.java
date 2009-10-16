package org.flexpay.sz.persistence;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountry;
import org.junit.Test;

public class TestApplicationConfig extends AbSpringBeanAwareTestCase {

	@Test
	public void testGetDefaultCountry() {

		getDefaultCountry();
	}
}
