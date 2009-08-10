package org.flexpay.eirc.service;

import static org.flexpay.ab.persistence.TestData.IVANOVA_27;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class TestServiceOrganizationService extends EircSpringBeanAwareTestCase {

	@Autowired
	private BuildingService buildingService;
	private ServiceOrganizationService serviceOrganizationService;

	@Test
	public void testUpdateServedBuilding() throws FlexPayExceptionContainer {

		ServedBuilding building = (ServedBuilding) buildingService.read(IVANOVA_27);
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
