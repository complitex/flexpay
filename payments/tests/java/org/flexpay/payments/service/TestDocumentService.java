package org.flexpay.payments.service;

import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

public class TestDocumentService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private DocumentService documentService;

	@Test (expected = Exception.class)
	public void testCreateDocument() {
		documentService.create(new Document());
	}

	@Test
	public void testListRegisteredDocuments() throws ParseException {

		List<Document> docs = documentService.listRegisteredPaymentDocuments(
				TestData.SRV_PROVIDER_CN, TestData.ORG_TSZH,
				new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("2100-12-31")));
		assertFalse("", docs.isEmpty());
	}
}
