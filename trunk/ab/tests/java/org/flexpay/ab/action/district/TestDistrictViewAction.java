package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictViewAction;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictViewAction action;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new District(TestData.DISTRICT_SOVETSKII.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

}
