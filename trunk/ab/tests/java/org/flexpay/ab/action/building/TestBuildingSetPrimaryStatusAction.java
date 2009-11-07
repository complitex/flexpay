package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingAddressSetPrimaryStatusAction;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingSetPrimaryStatusAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressSetPrimaryStatusAction action;

	@Test
	public void testExecute() throws Exception {

		action.setAddress(new BuildingAddress(4L));

		action.execute();
	}
}
