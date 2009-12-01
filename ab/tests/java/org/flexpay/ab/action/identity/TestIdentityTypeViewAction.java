package org.flexpay.ab.action.identity;

import org.flexpay.ab.actions.identity.IdentityTypeViewAction;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleIdentityType;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestIdentityTypeViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypeViewAction action;
	@Autowired
	private IdentityTypeDao identityTypeDao;

	@Test
	public void testAction() throws Exception {

		action.setIdentityType(new IdentityType(TestData.IDENTITY_TYPE_FIO));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setIdentityType(new IdentityType(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setIdentityType(new IdentityType(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullIdentityType() throws Exception {

		action.setIdentityType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctIdentityType() throws Exception {

		action.setIdentityType(new IdentityType(10902L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledIdentityType() throws Exception {

		IdentityType identityType = createSimpleIdentityType("3456");
		identityType.disable();
		identityTypeDao.create(identityType);

		action.setIdentityType(identityType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		identityTypeDao.delete(identityType);

	}

}