package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.TestData;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class TestServiceTypeHistoryBuilder extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceTypeHistoryBuilder historyBuilder;

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Test
	public void testPatchServiceType() {

		ServiceType original = serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA);
		Diff diff = historyBuilder.diff(null, original);
		ServiceType copy = new ServiceType();
		historyBuilder.patch(copy, diff);

		assertEquals("Bad service type code", original.getCode(), copy.getCode());
	}
}
