package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingDeleteAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingDeleteAction action;

	@Test
	public void testDeleteBuildings() throws Exception {

		action.setObjectIds(list(2L));

		assertEquals("Invalid action result", BuildingDeleteAction.REDIRECT_SUCCESS, action.execute());
	}
}
