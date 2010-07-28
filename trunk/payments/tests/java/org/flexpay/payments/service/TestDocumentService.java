package org.flexpay.payments.service;

import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class TestDocumentService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DocumentService documentService;

	@Autowired
	private SPService spService;

	@Test (expected = Exception.class)
	public void testCreateDocument() {
		documentService.create(new Document());
	}

	@Test
	public void testSearchDocuments() {

		List<Document> result;
		result = documentService.searchDocuments(org.flexpay.payments.persistence.TestData.OPERATION, 99L, new BigDecimal("10.00"), new BigDecimal("20.00") );
		assertNotNull("Result should not be null", result);
		assertTrue("Result must be be empty on test data", result.isEmpty());

		BigDecimal criteriaSum = new BigDecimal("1235.00");
		Long criteriaOperationId = 1L;
		Long criteriaServiceTypeId = 1L;
		result = documentService.searchDocuments(org.flexpay.payments.persistence.TestData.OPERATION, 1L, criteriaSum, criteriaSum);
		assertNotNull("Result should not be null", result);
		assertTrue("Result must not be empty on test data", !result.isEmpty());

		for (Document doc : result) {
			Service serv = spService.readFull(doc.getServiceStub());
			if (!doc.getOperation().getId().equals(criteriaOperationId) ||
				!serv.getServiceType().getId().equals(criteriaServiceTypeId) ||
				doc.getSum().compareTo(criteriaSum) != 0) {
				fail("Result contains documents which does not correspond criterias");
			}
		}
	}

	@Test
	public void testListRegisteredPaymentDocuments() throws ParseException {

		List<Document> result;
		result = documentService.listRegisteredPaymentDocuments(DateUtil.parseDate("2009-04-15"), DateUtil.parseDate("2009-04-16"));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must be be empty on test data", result.isEmpty());

		result = documentService.listRegisteredPaymentDocuments(DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must not be empty on test data", !result.isEmpty());

		result = documentService.listRegisteredPaymentDocuments( TestData.SRV_PROVIDER_CN, TestData.ORG_TSZH,
									new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("2100-12-31")));
		assertNotNull("Result should not be null", result);
		assertTrue("Result must not be empty on test data", !result.isEmpty());
	}


	@Test
	public void testGetCashboxServiceSum() throws ParseException {

		BigDecimal zeroSum = documentService.getCashboxServiceSum(TestData.CASHBOX_1, OperationStatus.REGISTERED, 1,
												DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", zeroSum);
        assertEquals("Sum must be zero", 0, zeroSum.compareTo(BigDecimal.ZERO));

		BigDecimal actualSum = documentService.getCashboxServiceSum(TestData.CASHBOX_1, OperationStatus.CREATED, 1,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", actualSum);
        assertEquals("Sum is bad", 0, actualSum.compareTo(new BigDecimal("1235.00")));
	}

	@Test
	public void testGetCashboxTotalSum() throws ParseException {

		BigDecimal zeroSum = documentService.getCashboxTotalSum(TestData.CASHBOX_2, OperationStatus.RETURNED,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", zeroSum);
        assertEquals("Sum must be zero", 0, zeroSum.compareTo(BigDecimal.ZERO));

		BigDecimal actualSum = documentService.getCashboxTotalSum(TestData.CASHBOX_1, OperationStatus.REGISTERED,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", actualSum);
        assertEquals("Sum is bad", 0, actualSum.compareTo(new BigDecimal("333.00")));
	}

	@Test
	public void testGetPaymentPointServiceSum() throws ParseException {

		BigDecimal zeroSum = documentService.getPaymentPointServiceSum(TestData.PAYMENT_POINT_1, OperationStatus.REGISTERED, 2,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", zeroSum);
        assertEquals("Sum must be zero", 0, zeroSum.compareTo(BigDecimal.ZERO));

		BigDecimal actualSum = documentService.getPaymentPointServiceSum(TestData.PAYMENT_POINT_1, OperationStatus.CREATED, 1,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", actualSum);
        assertEquals("Sum is bad", 0, actualSum.compareTo(new BigDecimal("1235.00")));
	}

	@Test
	public void testGetPaymentPointTotalSum()  throws ParseException {

		BigDecimal zeroSum = documentService.getPaymentPointServiceSum(TestData.PAYMENT_POINT_2, OperationStatus.REGISTERED, 2,
											DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", zeroSum);
        assertEquals("Sum must be zero", 0, zeroSum.compareTo(BigDecimal.ZERO));

		BigDecimal actualSum1 = documentService.getPaymentPointTotalSum(TestData.PAYMENT_POINT_1, OperationStatus.REGISTERED, DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", actualSum1);
        assertEquals("Sum is bad", 0, actualSum1.compareTo(new BigDecimal("220.00")));

		BigDecimal actualSum2 = documentService.getPaymentPointTotalSum(TestData.PAYMENT_POINT_1, OperationStatus.CREATED, DateUtil.parseDate("2009-04-10"), DateUtil.parseDate("2009-04-20"));
		assertNotNull("Sum must not be null", actualSum2);
        assertEquals("Sum is bad", 0, actualSum2.compareTo(new BigDecimal("1395.00")));
	}

	@Test
	public void testGetOperationServiceSum() {

		BigDecimal zeroSum = documentService.getOperationServiceSum(org.flexpay.payments.persistence.TestData.OPERATION, 9);
		assertNotNull("Sum must not be null", zeroSum);
        assertEquals("Sum must be zero", 0, zeroSum.compareTo(BigDecimal.ZERO));

		BigDecimal actualSum = documentService.getOperationServiceSum(org.flexpay.payments.persistence.TestData.OPERATION, 1);
		assertNotNull("Sum must not be null", actualSum);
        assertEquals("Sum is bad", 0, actualSum.compareTo(new BigDecimal("1235.00")));
	}

    @Test
	public void testDelete() {
		// attempt to delete document with non existent id must be ok
		documentService.delete(new Stub<Document>(9999L));
	}

}
