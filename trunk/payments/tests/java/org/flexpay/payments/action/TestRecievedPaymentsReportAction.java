package org.flexpay.payments.action;

import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.actions.reports.ReceivedPaymentsReportAction;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.List;
import java.util.Date;

public class TestRecievedPaymentsReportAction extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ReceivedPaymentsReportAction action;

	@Test
	public void testBuildReport() throws Exception {
		BeginDateFilter filter = new BeginDateFilter();
		filter.setStringDate("2009/04/14");
		action.setBeginDateFilter(filter);
		action.setOrganizationId(1L);
		action.setSubmitted("submitted");

		action.execute();

		List<Operation> operations = action.getOperations();
		assertFalse("No operations found", operations.isEmpty());

		// check operations are sorted by creation date
		Date minDate = ApplicationConfig.getPastInfinite();
		for (Operation operation : operations) {
			assertTrue("Invalid operations sort", minDate.compareTo(operation.getCreationDate()) <= 0);
			minDate = operation.getCreationDate();
		}
	}
}
