package org.flexpay.ab.action.identity;

import org.flexpay.ab.actions.identity.IdentityTypeDeleteAction;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleIdentityType;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestIdentityTypeDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypeDeleteAction action;
	@Autowired
	private IdentityTypeDao identityTypeDao;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testDeleteRegions() throws Exception {

		IdentityType identityType = createSimpleIdentityType("3456");
		identityTypeDao.create(identityType);

		action.setObjectIds(set(identityType.getId()));
		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		identityType = identityTypeDao.read(identityType.getId());
		assertTrue("Invalid status for identity type. Must be disabled", identityType.isNotActive());

		identityTypeDao.delete(identityType);
	}

}