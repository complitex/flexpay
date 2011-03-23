package org.flexpay.ab.action.town;

import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleTown;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestTownDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownDeleteAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeleteTowns() throws Exception {

		Town town = createSimpleTown("testName");
		townDao.create(town);

		action.setObjectIds(set(town.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		town = townDao.read(town.getId());
		assertTrue("Invalid status for town. Must be disabled", town.isNotActive());

		townDao.delete(town);
	}

}
