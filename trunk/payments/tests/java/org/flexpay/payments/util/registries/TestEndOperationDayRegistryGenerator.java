package org.flexpay.payments.util.registries;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
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
	@Qualifier("paymentPointService")
	PaymentPointService paymentPointService;
	@Autowired
	@Qualifier("organizationService")
	OrganizationService organizationService;
	@Autowired
	@Qualifier("registryService")
	RegistryService registryService;

	@Test
	public void testGenerate() throws Throwable {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date beginDate = df.parse("2009-03-14 00:00:00");
		Date endDate = df.parse("2009-07-07 23:59:59");

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(1L));
		if (paymentPoint == null) {
			log.error("Payment point with id - {} does not exist", 1L);
			return;
		}
		log.debug("Found paymentPoint - {}", paymentPoint);

		Organization organization = organizationService.readFull(new Stub<Organization>(4L));
		if (organization == null) {
			log.error("Organization with id - {} does not exist", 4L);
			return;
		}

		Registry registry = endOperationDayRegistryGenerator.generate(paymentPoint, organization, beginDate, endDate);

/*
		registryService.deleteRecords(Stub.stub(registry));
		registryService.delete(registry);
*/

	}

}
