package org.flexpay.payments.util.registries;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import java.util.Date;

public class TestExportBankPaymentsRegistry extends SpringBeanAwareTestCase {

	@Autowired
	ExportBankPaymentsRegistry exportBankPaymentsRegistry;
	@Autowired
	RegistryService registryService;

	@Test
	public void testExport() throws Throwable {

		Registry registry = registryService.read(new Stub<Registry>(1L));

		registry = exportBankPaymentsRegistry.export(registry);

		registryService.deleteRecords(Stub.stub(registry));
		registryService.delete(registry);

	}


}
