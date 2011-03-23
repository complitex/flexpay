package org.flexpay.payments.service;

import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.dao.ServiceDaoExt;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServiceDao extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	protected ServiceDaoExt serviceDaoExt;
	@Autowired
	protected SPService spService;
	@Autowired
	private ServiceProviderService providerService;
	@Autowired
	protected ServiceTypeService serviceTypeService;

	@Test
	public void testGetIntersectionServices() {

		// Find CN service provider
		ServiceProvider provider = providerService.getProvider(TestData.ORG_CN);
		assertNotNull("Cannot find service provider", provider);

		// Find service type by code
		ServiceType type = serviceTypeService.getServiceType(1);
		assertNotNull("Cannot find service type", type);

		List<Service> services = serviceDaoExt.findIntersectingServices(provider.getId(), type.getId(),
				ApplicationConfig.getPastInfinite(),
				ApplicationConfig.getFutureInfinite());
		assertEquals("Invalid number of services found", 1, services.size());
	}
}
