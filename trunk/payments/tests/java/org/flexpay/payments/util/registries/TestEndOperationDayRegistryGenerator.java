package org.flexpay.payments.util.registries;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class TestEndOperationDayRegistryGenerator extends SpringBeanAwareTestCase {

	@Autowired
	EndOperationDayRegistryGenerator endOperationDayRegistryGenerator;
	@Autowired
	RegistryService registryService;

	@Test
	public void testGenerate() throws Throwable {

		Date beginDate = new Date();
		Date endDate = new Date();

		Registry registry = registry = endOperationDayRegistryGenerator.generate(new Stub<PaymentPoint>(1L), new Stub<Organization>(1L), beginDate, endDate);

		registryService.deleteRecords(Stub.stub(registry));
		registryService.delete(registry);

	}


}
