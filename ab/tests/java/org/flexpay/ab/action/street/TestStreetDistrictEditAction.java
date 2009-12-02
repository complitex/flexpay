package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetDistrictEditAction;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetDistrictEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetDistrictEditAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testIncorrectId1() throws Exception {

		action.setStreet(new Street(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setStreet(new Street(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullStreet() throws Exception {

		action.setStreet(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctStreet() throws Exception {

		action.setStreet(new Street(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("testName22");
		street.disable();
		streetDao.create(street);

		action.setStreet(street);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		streetDaoExt.deleteStreet(action.getStreet());

	}

	@Test
	public void testNotSubmit() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("DistrictNames parameter must not be empty", !action.getDistrictNames().isEmpty());

	}

	@Test
	public void testSubmitNullObjectIds() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setObjectIds(null);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

	}

	@Test
	public void testSubmit() throws Exception {

		Street street = createSimpleStreet("testNameS");
		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setObjectIds(set(1L, 2L, 3L));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Street districts set must not be empty", !action.getStreet().getDistricts().isEmpty());

		streetDaoExt.deleteStreet(action.getStreet());

	}

}
