package org.flexpay.ab.action.town;

import org.flexpay.ab.dao.TownTypeDao;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleTownType;
import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;
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
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeleteTownTypes() throws Exception {

		TownType townType = createSimpleTownType("3456");
		townTypeDao.create(townType);

		action.setObjectIds(set(townType.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		townType = townTypeDao.read(townType.getId());
		assertTrue("Invalid status for town type. Must be disabled", townType.isNotActive());

		townTypeDao.delete(townType);
	}

}
