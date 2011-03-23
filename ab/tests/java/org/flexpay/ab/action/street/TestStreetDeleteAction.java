package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestStreetDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetDeleteAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeleteStreets() throws Exception {

		Street street = createSimpleStreet("testName");
		streetDao.create(street);

		action.setObjectIds(set(street.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		street = streetDao.read(street.getId());
		assertTrue("Invalid status for street. Must be disabled", street.isNotActive());

		streetDaoExt.deleteStreet(street);

	}

}
