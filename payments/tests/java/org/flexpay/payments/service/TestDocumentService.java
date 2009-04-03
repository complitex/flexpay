package org.flexpay.payments.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.payments.persistence.Document;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDocumentService  extends SpringBeanAwareTestCase {

	@Autowired
	private DocumentService documentService;

	@Test (expected = Exception.class)
	public void testGetDocument() {
		documentService.save(new Document());
	}
}
