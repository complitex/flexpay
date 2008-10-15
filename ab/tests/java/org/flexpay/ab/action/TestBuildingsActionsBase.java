package org.flexpay.ab.action;

import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.UserPreferences;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestBuildingsActionsBase extends SpringBeanAwareTestCase {

	private BuildingService buildingService;
	private BuildingsActionsBase base;

	@Autowired
	public void setBuildingService(@Qualifier ("buildingService") BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Autowired
	public void setBase(@Qualifier ("listApartments") BuildingsActionsBase base) {
		this.base = base;
	}

	@Test
	public void testGetBuildingNumber() throws Exception {

		Buildings buildings = buildingService.readFull(new Stub<Buildings>(1L));
		if (buildings == null) {
			fail("No building found with id=1");
			return;
		}

		String number = base.getBuildingNumber(buildings.getBuildingAttributes());

		assertNotNull("Number is not defined", number);
	}

	@Before
	public void prepareBase() throws FlexPayException {
		UserPreferences prefs = new UserPreferences();
		prefs.setLocale(ApplicationConfig.getDefaultLocale());
		base.setUserPreferences(prefs);
	}
}
