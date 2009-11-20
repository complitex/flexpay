package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownViewAction;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownViewAction action;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new Town(TestData.TOWN_NSK.getId()));

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

}
