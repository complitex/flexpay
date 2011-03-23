package org.flexpay.sz.persistence;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountry;

public class TestApplicationConfig extends AbSpringBeanAwareTestCase {

	@Test
	public void testGetDefaultCountry() {

		getDefaultCountry();
	}
}
