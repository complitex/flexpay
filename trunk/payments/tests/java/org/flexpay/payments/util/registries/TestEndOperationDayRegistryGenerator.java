package org.flexpay.payments.util.registries;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEndOperationDayRegistryGenerator extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	EndOperationDayRegistryGenerator endOperationDayRegistryGenerator;
	@Autowired
	@Qualifier("registryService")
	RegistryService registryService;

	@Test
	public void testGenerate() throws Throwable {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date beginDate = df.parse("2009-06-07 00:00:00");
		Date endDate = df.parse("2009-06-07 23:59:59");

		Registry registry = endOperationDayRegistryGenerator.generate(new Stub<PaymentPoint>(2L), new Stub<Organization>(4L), beginDate, endDate);

		registryService.deleteRecords(Stub.stub(registry));
		registryService.delete(registry);

	}

}
