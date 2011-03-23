package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.importexport.impl.ClassToTypeRegistryPayments;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class TestServiceTypeHistoryHandler extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceTypeHistoryHandler handler;

	private ClassToTypeRegistryPayments typeRegistry = new ClassToTypeRegistryPayments();

	@Test
	public void testSupports() {

		Diff serviceTypeDiff = new Diff();
		serviceTypeDiff.setObjectType(typeRegistry.getType(ServiceType.class));
		assertTrue("Handler must support this class: " + ServiceType.class, handler.supports(serviceTypeDiff));
	}	
}
