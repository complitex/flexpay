package org.flexpay.payments.action.operations;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.actions.operations.OperationsListAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestOperationsListAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OperationsListAction action;

	@Test
	public void testBadDateInterval() throws Exception {

		action.setCashboxId(1L);
		action.setFilterSubmitted("filterSubmitted");
		action.getBeginDateFilter().setDate(DateUtil.parseDate("2009/12/12"));
		action.getEndDateFilter().setDate(DateUtil.parseDate("2009/01/01"));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}

	@Test
	public void testBadSummInterval() throws Exception {

		action.setCashboxId(1L);
		action.setFilterSubmitted("filterSubmitted");
		action.setMinimalSumm("100.00");
		action.setMaximalSumm("10.00");
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}
}
