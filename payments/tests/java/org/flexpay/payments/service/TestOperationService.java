package org.flexpay.payments.service;

import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestOperationService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OperationService operationService;

	@Test
	public void testListOperations() throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date beginDate = df.parse("2009-04-14 00:00:00");
		Date endDate = df.parse("2009-04-14 23:59:59");

		List<Operation> operations = operationService.listReceivedPayments(TestData.PAYMENT_POINT_1, beginDate, endDate);
		assertFalse("No operations found", operations.isEmpty());
	}
}
