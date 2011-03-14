package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetViewAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new Street(TestData.IVANOVA));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setObject(new Street(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setObject(new Street(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullStreet() throws Exception {

		action.setObject(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDefunctStreet() throws Exception {

		action.setObject(new Street(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("testName111");
		street.disable();
		streetDao.create(street);

		action.setObject(street);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetDaoExt.deleteStreet(action.getObject());
	}

}
