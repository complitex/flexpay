package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownTypeViewAction;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTownType;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownTypeViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownTypeViewAction action;
	@Autowired
	private TownTypeDao townTypeDao;

	@Test
	public void testAction() throws Exception {

		action.setTownType(new TownType(TestData.TOWN_TYPE_CITY));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setTownType(new TownType(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setTownType(new TownType(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullTownType() throws Exception {

		action.setTownType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctTownType() throws Exception {

		action.setTownType(new TownType(10902L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledTownType() throws Exception {

		TownType townType = createSimpleTownType("3456");
		townType.disable();
		townTypeDao.create(townType);

		action.setTownType(townType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		townTypeDao.delete(townType);

	}

}
