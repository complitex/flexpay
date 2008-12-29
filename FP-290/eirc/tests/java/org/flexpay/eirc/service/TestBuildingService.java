package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import static org.springframework.dao.support.DataAccessUtils.intResult;

public class TestBuildingService extends SpringBeanAwareTestCase {

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		Number nBuildings = intResult(hibernateTemplate.find("select count(*) from Building"));
		Number nEircBuildings = intResult(hibernateTemplate.find("select count(*) from ServedBuilding"));

		assertEquals("All buildings should be the ServedBuilding's", nBuildings.longValue(), nEircBuildings.longValue());
	}
}
