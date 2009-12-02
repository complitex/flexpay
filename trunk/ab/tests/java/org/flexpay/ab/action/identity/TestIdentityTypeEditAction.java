package org.flexpay.ab.action.identity;

import org.flexpay.ab.actions.identity.IdentityTypeEditAction;
import org.flexpay.ab.dao.IdentityTypeDao;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleIdentityType;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestIdentityTypeEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private IdentityTypeEditAction action;
	@Autowired
	private IdentityTypeDao identityTypeDao;

	@Test
	public void testNullIdentityType() throws Exception {

		action.setIdentityType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullIdentityTypeId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setIdentityType(new IdentityType(0L));
		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		action.setIdentityType(new IdentityType(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		action.setIdentityType(new IdentityType(TestData.IDENTITY_TYPE_FIO));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid names size for different languages", ApplicationConfig.getLanguages().size(), action.getNames().size());

	}

	@Test
	public void testIncorrectData1() throws Exception {

		action.setIdentityType(new IdentityType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testEditDefunctIdentityType() throws Exception {

		action.setIdentityType(new IdentityType(121212L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testEditDisabledIdentityType() throws Exception {

		IdentityType identityType = createSimpleIdentityType("type2");
		identityType.disable();
		identityTypeDao.create(identityType);

		action.setIdentityType(identityType);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		identityTypeDao.delete(identityType);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		action.setIdentityType(new IdentityType(0L));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("555"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid identity type id", action.getIdentityType().getId() > 0);

		identityTypeDao.delete(action.getIdentityType());
	}

	@Test
	public void testEditSubmit() throws Exception {

		IdentityType identity = createSimpleIdentityType("type1");
		identityTypeDao.create(identity);

		action.setIdentityType(identity);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setNames(initNames("999"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		String name = action.getIdentityType().getDefaultTranslation().getName();
		assertEquals("Invalid identity type name value", "999", name);

		identityTypeDao.delete(action.getIdentityType());
	}

}
