package org.flexpay.payments.action.registry;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.actions.registry.RegistryDeliveryHistoryAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegistryDeliveryHistoryAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistryDeliveryHistoryAction action;
	@Test
	public void testExecute() throws Exception {

		// test wrong dates
/*
		action.setFilterSubmitted("submitted");
		action.setBeginDate("2009/12/12");
		action.setEndDate("2009/01/01");
*/
		action.getBeginDateFilter().setDate(DateUtil.parseDate("2009/12/12"));
		action.getEndDateFilter().setDate(DateUtil.parseDate("2009/01/01"));
		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}
}
