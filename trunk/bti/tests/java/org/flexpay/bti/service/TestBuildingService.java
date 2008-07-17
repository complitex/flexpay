package org.flexpay.bti.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.junit.Ignore;import static org.junit.Assert.assertEquals;
import org.springframework.dao.support.DataAccessUtils;
import static org.springframework.dao.support.DataAccessUtils.intResult;

public class TestBuildingService extends SpringBeanAwareTestCase {

	@Test
	public void testAllBuildingsAreValid() throws Throwable {
		int nBuildings = intResult(hibernateTemplate.find("select count(*) from Building"));
		int nBtiBuildings = intResult(hibernateTemplate.find("select count(*) from Building"));
		assertEquals("All building should be of the same type", nBuildings, nBtiBuildings);
	}
}
