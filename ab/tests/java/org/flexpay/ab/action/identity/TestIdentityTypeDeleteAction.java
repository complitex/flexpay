package org.flexpay.ab.action.identity;

import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleIdentityType;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestIdentityTypeDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypeDeleteAction action;
	@Autowired
	private IdentityTypeDao identityTypeDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testDeleteIdentityTypes() throws Exception {

		IdentityType identityType = createSimpleIdentityType("3456");
		identityTypeDao.create(identityType);

		action.setObjectIds(set(identityType.getId(), -210L, 23455L, 0L, null));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		identityType = identityTypeDao.read(identityType.getId());
		assertTrue("Invalid status for identity type. Must be disabled", identityType.isNotActive());

		identityTypeDao.delete(identityType);
	}

}
