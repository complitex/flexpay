package org.flexpay.payments.util.registries;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestExportBankPaymentsRegistry extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	ExportBankPaymentsRegistry exportBankPaymentsRegistry;
	@Autowired
	@Qualifier("registryService")
	RegistryService registryService;

	@Test
	public void testExport() throws Throwable {

		Registry registry = registryService.read(new Stub<Registry>(4L));

		registry = exportBankPaymentsRegistry.export(registry);

/*
		registryService.deleteRecords(Stub.stub(registry));
		registryService.delete(registry);
*/

	}


}
