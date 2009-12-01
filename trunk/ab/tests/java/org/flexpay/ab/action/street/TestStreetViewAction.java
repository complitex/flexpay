package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetViewAction;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleStreet;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetViewAction action;
	@Autowired
	private StreetDao streetDao;

	@Test
	public void testCorrectData() throws Exception {

		action.setObject(new Street(TestData.IVANOVA.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setObject(new Street(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setObject(new Street(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullStreet() throws Exception {

		action.setObject(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctStreet() throws Exception {

		action.setObject(new Street(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledStreet() throws Exception {

		Street street = createSimpleStreet("testName111");
		street.disable();

		streetDao.create(street);

		action.setObject(street);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		streetDao.delete(action.getObject());

	}

}
