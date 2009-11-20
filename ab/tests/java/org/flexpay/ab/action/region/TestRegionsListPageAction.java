package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionsListPageAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionsListPageAction action;

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testAction2() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

}
