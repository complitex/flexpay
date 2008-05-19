package org.flexpay.ab.action;

import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.exception.FlexPayException;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class TestBuildingsActionsBase extends SpringBeanAwareTestCase {

	@Autowired
	protected BuildingService buildingService;

	private BuildingsActionsBase base;

	@Autowired
	public void setBase(@Qualifier("listApartments")BuildingsActionsBase base) {
		this.base = base;
	}

	@Test
	public void testGetBuildingNumber() throws Exception {

		Buildings buildings = buildingService.readFull(194L);
		String number = base.getBuildingNumber(buildings.getBuildingAttributes());

		System.out.println("Number: " + number);
		assertNotNull("Number is not defined", number);
	}

	@Before
	public void prepareBase() throws FlexPayException {
		UserPreferences prefs = new UserPreferences();
		prefs.setLocale(ApplicationConfig.getInstance().getDefaultLanguage().getLocale());
		base.setUserPreferences(prefs);
	}
}
