package org.flexpay.payments.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.dao.ServiceDaoExt;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.service.SPService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class TestServiceDao extends SpringBeanAwareTestCase {

	@Autowired
	protected ServiceDaoExt serviceDaoExt;
	@Autowired
	@Qualifier ("spService")
	protected SPService spService;
	@Autowired
	private ServiceProviderService providerService;
	@Autowired
	protected ServiceTypeService serviceTypeService;

	@Test
	public void testGetIntersectionServices() {

		// Find CN service provider
		ServiceProvider provider = providerService.getProvider(4L);
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
