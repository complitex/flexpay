package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownTypeDeleteAction;
import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTownType;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownTypeDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownTypeDeleteAction action;
	@Autowired
	private TownTypeDao townTypeDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteTownTypes() throws Exception {

		TownType townType = createSimpleTownType("3456");
		townTypeDao.create(townType);

		action.setObjectIds(set(townType.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		townType = townTypeDao.read(townType.getId());
		assertTrue("Invalid status for town type. Must be disabled", townType.isNotActive());

		townTypeDao.delete(townType);
	}

}
