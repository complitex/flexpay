package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.importexport.impl.ClassToTypeRegistryPayments;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class TestServiceHistoryHandler extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceHistoryHandler serviceHistoryHandler;

	private ClassToTypeRegistryPayments typeRegistry = new ClassToTypeRegistryPayments();

	@Test
	public void testSupports() {

		Diff serviceDiff = new Diff();
		serviceDiff.setObjectType(typeRegistry.getType(Service.class));
		assertTrue("Handler must support this class: " + Service.class, serviceHistoryHandler.supports(serviceDiff));
	}
}
