package org.flexpay.bti.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.junit.Ignore;

public class TestBuildingService extends SpringBeanAwareTestCase {

	@Test
	@Ignore
	public void testAllBuildingsAreValid() throws Throwable {
		Number nBuildings = (Number) hibernateTemplate.find("select count(*) from Building").get(0);
		Number nBtiBuildings = (Number) hibernateTemplate.find("select count(*) from Building").get(0);
	}
}
