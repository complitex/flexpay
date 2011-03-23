package org.flexpay.payments.service;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.fail;

public class TestRegistryDeliveryHistoryService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private RegistryDeliveryHistoryService registryDeliveryHistoryService;

	@Test
	public void testService() {

		try {
			RegistryDeliveryHistory history = new RegistryDeliveryHistory();
			history.setRegistry(new Registry());
			registryDeliveryHistoryService.create(history);
		} catch (Exception e) {
			return;
		}

		fail("Creation delivery history with no registry reference must not be allowed.");
	}
}
