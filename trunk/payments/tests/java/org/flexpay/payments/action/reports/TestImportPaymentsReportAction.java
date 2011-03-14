package org.flexpay.payments.action.reports;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.util.DateUtil;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestImportPaymentsReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ImportPaymentsReportAction action;

	@Test
	public void testExecute() throws Exception {

		// test not submit
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		// test wrong dates
		action.setSubmitted("submitted");
		action.setBeginDateFilter(new BeginDateFilter(DateUtil.parseDate("2009/12/12")));
		action.setEndDateFilter(new EndDateFilter(DateUtil.parseDate("2009/01/01")));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Action must give an error", action.hasActionErrors());
	}	
}
