package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownViewAction;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTown;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownViewAction action;
	@Autowired
	private TownDao townDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new Town(TestData.TOWN_NSK));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setObject(new Town(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setObject(new Town(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullTown() throws Exception {

		action.setObject(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctTown() throws Exception {

		action.setObject(new Town(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledTown() throws Exception {

		Town town = createSimpleTown("testName");
		town.disable();

		townDao.create(town);

		action.setObject(town);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		townDao.delete(action.getObject());

	}

}
