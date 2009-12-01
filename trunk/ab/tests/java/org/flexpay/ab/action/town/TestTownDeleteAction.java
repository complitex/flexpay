package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownDeleteAction;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTown;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownDeleteAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteTowns() throws Exception {

		Town town = createSimpleTown("testName");

		townDao.create(town);

		action.setObjectIds(set(town.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		town = townDao.read(town.getId());
		assertTrue("Invalid status for town. Must be disabled", town.isNotActive());

		townDao.delete(town);

	}

}
