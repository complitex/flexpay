package org.flexpay.payments.util.registries;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.test.TestData;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestExportBankPaymentsRegistry extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	EndOperationDayRegistryGenerator endOperationDayRegistryGenerator;
	@Autowired
	ExportBankPaymentsRegistry exportBankPaymentsRegistry;
	@Autowired
	PaymentPointService paymentPointService;
	@Autowired
	OrganizationService organizationService;
	@Autowired
	RegistryService registryService;

	@Test
	public void testExport() throws Throwable {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date beginDate = df.parse("2009-04-14 00:00:00");
		Date endDate = df.parse("2009-04-14 23:59:59");

		PaymentPoint paymentPoint = paymentPointService.read(TestData.PAYMENT_POINT_1);
		assertNotNull("Payment point with id - 1 does not exist", paymentPoint);

		Organization organization = organizationService.readFull(TestData.ORG_CN);
		assertNotNull("Organization with id - 4 does not exist", organization);

		Registry registry = endOperationDayRegistryGenerator.generate(paymentPoint, organization, beginDate, endDate);
		assertNotNull("Operation day registry generation failed", registry);
		exportBankPaymentsRegistry.generateAndAttachFile(registry);

//		registryService.deleteRecords(Stub.stub(registry));
//		registryService.delete(registry);
	}

}
