package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.AddressAttributeTypeViewAction;
import org.flexpay.ab.dao.AddressAttributeTypeDao;
import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleAddressAttributeType;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAddressAttributeTypeViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private AddressAttributeTypeViewAction action;
	@Autowired
	private AddressAttributeTypeDao attributeTypeDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setAttributeType(new AddressAttributeType(TestData.ATTRIBUTE_TYPE_NUMBER));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setAttributeType(new AddressAttributeType(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setAttributeType(new AddressAttributeType(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullAttributeType() throws Exception {

		action.setAttributeType(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctAttributeType() throws Exception {

		action.setAttributeType(new AddressAttributeType(100056L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledAttributeType() throws Exception {

		AddressAttributeType attributeType = createSimpleAddressAttributeType("2222");
		attributeType.disable();
		attributeTypeDao.create(attributeType);

		action.setAttributeType(attributeType);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		attributeTypeDao.delete(action.getAttributeType());

	}

}