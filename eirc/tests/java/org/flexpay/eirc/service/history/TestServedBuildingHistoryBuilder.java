package org.flexpay.eirc.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import static org.flexpay.ab.persistence.TestData.IVANOVA_2;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.orgs.persistence.TestData.SRV_ORG_UCHASTOK45;
import static org.junit.Assert.*;

public class TestServedBuildingHistoryBuilder extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("buildingHistoryBuilder")
	private HistoryBuilder<Building> historyBuilder;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private ObjectsFactory factory;
	private ServiceOrganizationService serviceOrganizationService;

	@Test
	public void testPatchServiceOrganization() throws FlexPayExceptionContainer {

		ServedBuilding building = (ServedBuilding) buildingService.readFull(IVANOVA_2);
		assertNotNull("Building IVANOVA_2 not found", building);

		Diff diff = historyBuilder.diff(null, building);

		ServedBuilding copy = (ServedBuilding) factory.newBuilding();
		historyBuilder.patch(copy, diff);

		assertEquals("Invalid service org ref patch",
				building.getServiceOrganizationStub(), copy.getServiceOrganizationStub());

		ServiceOrganization organization = serviceOrganizationService.read(SRV_ORG_UCHASTOK45);
		assertNotNull("Service organization SRV_ORG_UCHASTOK45 not found", organization);

		building.setServiceOrganization(organization);
		serviceOrganizationService.updateServedBuilding(building);

		building = (ServedBuilding) buildingService.readFull(IVANOVA_2);
		assertNotNull("Building IVANOVA_2 not found after update", building);

		diff = historyBuilder.diff(copy, building);
		historyBuilder.patch(copy, diff);
		assertEquals("Invalid service org ref patch after update",
				building.getServiceOrganizationStub(), copy.getServiceOrganizationStub());

		serviceOrganizationService.removeServedBuildings(set(building.getId()));
		building = (ServedBuilding) buildingService.readFull(IVANOVA_2);
		assertNotNull("Building IVANOVA_2 not found after second update", building);

		diff = historyBuilder.diff(copy, building);
		historyBuilder.patch(copy, diff);
		assertNull("Invalid service org ref patch after delete", copy.getServiceOrganization());

	}

	@Before
	public void setupServices() {
		serviceOrganizationService = (ServiceOrganizationService) applicationContext
				.getBean("eircServiceOrganizationService");
		Assert.notNull(serviceOrganizationService, "eircServiceOrganizationService not found");
	}
}
