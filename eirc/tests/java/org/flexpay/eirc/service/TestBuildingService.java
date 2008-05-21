package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestBuildingService extends SpringBeanAwareTestCase {

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		Number nBuildings = (Number) hibernateTemplate.find("select count(*) from Building").get(0);
		Number nBtiBuildings = (Number) hibernateTemplate.find("select count(*) from ServedBuilding").get(0);

		assertEquals("Buildings setup is invalid", nBuildings.longValue(), nBtiBuildings.longValue());
	}
}
