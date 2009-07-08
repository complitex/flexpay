package org.flexpay.payments.action.reports;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.payments.actions.reports.DayReceivedPaymentsReportAction;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TestReceivedPaymentsReportAction extends PaymentsSpringBeanAwareTestCase  {

	@Autowired
	private DayReceivedPaymentsReportAction action;

	@Test
	public void testBuildPrintForm() throws Exception {

		BeginDateFilter beginDateFilter = new BeginDateFilter();
		beginDateFilter.setStringDate("2009/04/14");

		action.setBeginDateFilter(beginDateFilter);
		action.setSubmitted("submitted");
		action.setCashboxId(1L);

		assertEquals("Action execute failed", FPActionSupport.FILE, action.execute());

		System.out.println("File: " + action.getReport().getNameOnServer());		
	}
}
