package org.flexpay.ab.action.street;

import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetDistrictRelation;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class TestStreetDistrictEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetDistrictEditAction action;
	@Autowired
	private StreetDao streetDao;
	@Autowired
	private StreetDaoExt streetDaoExt;

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

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

		Street street = createSimpleStreet("testName111");
		street.disable();
		streetDao.create(street);

		action.setStreet(street);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		streetDaoExt.deleteStreet(action.getStreet());
	}

	@Test
	public void testNotSubmit() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("DistrictNames parameter must not be empty", action.getDistrictNames().isEmpty());

	}

	@Test
	public void testSubmitNullObjectIds() throws Exception {

		action.setStreet(new Street(TestData.IVANOVA));
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setObjectIds(null);
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testSubmit() throws Exception {

		Street street = createSimpleStreet("testNameS");
		Set<StreetDistrictRelation> streetDistricts = set();
		streetDistricts.add(new StreetDistrictRelation(street, new District(TestData.DISTRICT_SOVETSKIY)));
		streetDistricts.add(new StreetDistrictRelation(street, new District(TestData.DISTRICT_ZAELCOVSKIY)));
		street.setStreetDistricts(streetDistricts);
		streetDao.create(street);

		action.setStreet(street);
		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		action.setSubmitted("");
		action.setObjectIds(set(TestData.DISTRICT_ZAELCOVSKIY.getId(), TestData.DISTRICT_DZERGINSKIY.getId(), -10L, 0L, 123323L, null));
		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertFalse("Street districts set must not be empty", action.getStreet().getStreetDistricts().isEmpty());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		streetDaoExt.deleteStreet(action.getStreet());

	}

}
