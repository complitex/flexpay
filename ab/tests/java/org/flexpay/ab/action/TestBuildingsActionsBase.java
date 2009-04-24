package org.flexpay.ab.action;

import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestBuildingsActionsBase extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("buildingService")
	private BuildingService buildingService;
	@Autowired
	@Qualifier ("apartmentsListAction")
	private BuildingsActionsBase base;

	@Test
	public void testGetBuildingNumber() throws Exception {

		BuildingAddress buildingAddress = buildingService.readFull(new Stub<BuildingAddress>(1L));
		if (buildingAddress == null) {
			fail("No building found with id=1");
			return;
		}

		String number = base.getBuildingNumber(buildingAddress.getBuildingAttributes());

		assertNotNull("Number is not defined", number);
	}

	@Before
	public void prepareBase() throws FlexPayException {
		UserPreferences prefs = new UserPreferences();
		prefs.setLocale(ApplicationConfig.getDefaultLocale());
		base.setUserPreferences(prefs);
	}
}
