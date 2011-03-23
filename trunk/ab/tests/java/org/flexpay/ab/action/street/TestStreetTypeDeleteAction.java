package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleStreetType;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestStreetTypeDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetTypeDeleteAction action;
	@Autowired
	private StreetTypeDao streetTypeDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeleteStreetTypes() throws Exception {

		StreetType streetType = createSimpleStreetType("3456");
		streetTypeDao.create(streetType);

		action.setObjectIds(set(streetType.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetType = streetTypeDao.read(streetType.getId());
		assertTrue("Invalid status for street type. Must be disabled", streetType.isNotActive());

		streetTypeDao.delete(streetType);
	}

}