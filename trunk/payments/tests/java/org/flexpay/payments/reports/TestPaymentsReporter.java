package org.flexpay.payments.reports;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.reports.payments.PaymentsPrintInfoData;
import org.flexpay.payments.reports.payments.PaymentsReporter;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class TestPaymentsReporter extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private PaymentsReporter paymentsReporter;
	
	@Test
	public void testGetPaymentPrintFormData() {

		assertNull("Reporter must not give result data for non-existent operations", paymentsReporter.getPaymentPrintFormData(new Stub<Operation>(999L)));
		assertNotNull("Reporter must not give null result data for correct operation", paymentsReporter.getPaymentPrintFormData(new Stub<Operation>(1L)));
	}

	@Test
	public void testGetReceivedPaymentsPrintFormData() throws Exception {

		Date beginDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 00:00:00");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 23:59:59");

		PaymentsPrintInfoData data = paymentsReporter.getReceivedPaymentsPrintFormData(beginDate, endDate, TestData.CASHBOX_1, Locale.getDefault());
		assertTrue("Bad report data", data.getOperationDetailses().size() > 0);
	}

	@Test
	public void testGetReturnedPaymentsPrintFormData() throws Exception {

		Date beginDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 00:00:00");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2009-04-14 23:59:59");

		PaymentsPrintInfoData data = paymentsReporter.getReceivedPaymentsPrintFormData(beginDate, endDate, TestData.CASHBOX_1, Locale.getDefault());
		assertTrue("Bad report data", data.getOperationDetailses().size() > 0);
	}

}
