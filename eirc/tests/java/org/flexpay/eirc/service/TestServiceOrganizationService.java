package org.flexpay.eirc.service;

import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import static org.flexpay.ab.persistence.TestData.IVANOVA_27;
import static org.junit.Assert.assertNotNull;

public class TestServiceOrganizationService extends EircSpringBeanAwareTestCase {

	@Autowired
	private BuildingService buildingService;
	private ServiceOrganizationService serviceOrganizationService;

	@Test
	public void testUpdateServedBuilding() throws FlexPayExceptionContainer {

		ServedBuilding building = (ServedBuilding) buildingService.readFull(IVANOVA_27);
		assertNotNull("Building IVANOVA_27 not found", building);

		serviceOrganizationService.updateServedBuilding(building);
	}

	@Before
	public void setupServices() {
		serviceOrganizationService = (ServiceOrganizationService) applicationContext
				.getBean("eircServiceOrganizationService");
		Assert.notNull(serviceOrganizationService, "eircServiceOrganizationService not found");
	}
}
