package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingDeleteAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;

public class TestBuildingDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingDeleteAction action;

	@Test
	public void testDeleteBuildings() throws Exception {

		action.setObjectIds(set(2L));

		assertEquals("Invalid action result", BuildingDeleteAction.SUCCESS, action.execute());
	}

}
