package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingDeleteAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;

public class TestBuildingDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingDeleteAction action;

	@Test
	public void testDeleteBuildings() throws Exception {

		action.setObjectIds(set(TestData.ADDR_IVANOVA_2.getId()));

		assertEquals("Invalid action result", BuildingDeleteAction.SUCCESS, action.execute());
	}

	@After
	public void enableBuildings() {
		hibernateTemplate.bulkUpdate("update Building set status=0 where id=?", TestData.ADDR_IVANOVA_2.getId());
	}
}
