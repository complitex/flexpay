package org.flexpay.eirc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestServiceDao extends SpringBeanAwareTestCase {

	@Autowired
	protected ServiceDaoExt serviceDaoExt;
	@Autowired
	protected SPService spService;
	@Autowired
	protected ServiceTypeService serviceTypeService;

	@Test
	public void testGetIntersectionServices() {

		// Find CN service provider
		ServiceProvider provider = spService.getProvider(10L);
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
