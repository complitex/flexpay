package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestOperationService extends PaymentsSpringBeanAwareTestCase {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private OperationService operationService;

	@Test
	public void testListPaymentOperations() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-13 23:59:59");
		List<Operation> result = operationService.listPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testListLastPaymentOperations() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-13 23:59:59");
		List<Operation> result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	public void testListLastPaymentOperationsForPaymentPoint() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.listLastPaymentOperationsForPaymentPoint(TestData.PAYMENT_POINT_3, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listLastPaymentOperationsForPaymentPoint(TestData.PAYMENT_POINT_1, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	public void testListLastPaymentOperationsForCashbox() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.listLastPaymentOperationsForCashbox(TestData.CASHBOX_3, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listLastPaymentOperationsForCashbox(TestData.CASHBOX_1, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testListReceivedPaymentsForCashbox() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.listReceivedPaymentsForCashbox(TestData.CASHBOX_3, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listReceivedPaymentsForCashbox(TestData.CASHBOX_1, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	public void testListReceivedPaymentsForPaymentPoint() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	public void testListReceivedPaymentsForOrganization() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-10 00:00:00");
		endDate = dateFormat.parse("2009-04-20 23:59:59");
		result = operationService.listLastPaymentOperations(beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testReturnedPayments() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-14 14:22:00");
		Date endDate = dateFormat.parse("2009-04-14 23:59:59");
		List<Operation> result = operationService.listReturnedPaymentsForCashbox(TestData.CASHBOX_1, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		beginDate = dateFormat.parse("2009-04-14 14:20:00");
		endDate = dateFormat.parse("2009-04-14 14:21:59");
		result = operationService.listReturnedPaymentsForCashbox(TestData.CASHBOX_1, beginDate, endDate);
		assertNotNull("Result mustn't be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testSearchDocuments() throws ParseException {

		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.searchDocuments(TestData.CASHBOX_1, 99L, beginDate, endDate,
								new BigDecimal("10.00"), new BigDecimal("20.00"), new Page<Operation>(10));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must be be empty on test data", result.isEmpty());

		BigDecimal criteriaSum = new BigDecimal("1235.00");
		result = operationService.searchDocuments(TestData.CASHBOX_1, 1L, beginDate, endDate,
								criteriaSum, criteriaSum, new Page<Operation>(10));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must not be empty on test data", !result.isEmpty());
	}

	@Test
	public void testSearchOperations() throws ParseException {


		Date beginDate = dateFormat.parse("2009-04-10 00:00:00");
		Date endDate = dateFormat.parse("2009-04-20 23:59:59");
		List<Operation> result = operationService.searchOperations(TestData.CASHBOX_1, beginDate, endDate,
								new BigDecimal("10.00"), new BigDecimal("20.00"), new Page<Operation>(10));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must be be empty on test data", result.isEmpty());

		BigDecimal criteriaSum = new BigDecimal("1395.00");
		result = operationService.searchOperations(TestData.CASHBOX_1, beginDate, endDate,
								criteriaSum, criteriaSum, new Page<Operation>(10));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must not be empty on test data", !result.isEmpty());
	}

	@Test
	public void testCreateBlankOperation() {

		Operation operation =  null;
		try {
			operation = operationService.createBlankOperation("test", TestData.CASHBOX_1);
			assertTrue("Created operation has bad status", operation.getOperationStatus().getCode() == OperationStatus.BLANK);
		} catch (FlexPayException e) {
			fail("Error creating blank operation: " + e.getMessage());
		}

		operationService.delete(Stub.stub(operation));
	}

	@Test
	public void testDelete() {
		// attempt to delete operation with non existent id must be ok
		operationService.delete(new Stub<Operation>(9999L));
	}
}
