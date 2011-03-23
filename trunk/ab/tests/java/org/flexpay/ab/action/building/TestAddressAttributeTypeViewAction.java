package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleAddressAttributeType;
import static org.junit.Assert.*;

public class TestAddressAttributeTypeViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeViewAction action;
	@Autowired
	private AddressAttributeTypeDao attributeTypeDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setAttributeType(new AddressAttributeType(TestData.ATTRIBUTE_TYPE_NUMBER));

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

		action.setAttributeType(new AddressAttributeType(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullAttributeType() throws Exception {

		action.setAttributeType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDefunctAttributeType() throws Exception {

		action.setAttributeType(new AddressAttributeType(100056L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDisabledAttributeType() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeType.disable();
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		attributeTypeDao.delete(action.getAttributeType());

	}

}