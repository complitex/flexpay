package org.flexpay.ab.action.filter;

import org.flexpay.common.action.filter.FilterAjaxAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentFilterAjaxAction action;
	@Autowired
	private ApartmentService apartmentService;

	@Test
	public void testPrerequestNullApartmentId() throws Exception {

		action.setPreRequest(true);

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestIncorrectApartmentId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestDefunctApartment() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.IVANOVA_27_330.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.IVANOVA_27_330.getId(), action.getUserPreferences().getApartmentFilter());

		assertEquals("Invalid filter string value", apartmentService.getApartmentNumber(TestData.IVANOVA_27_330), action.getFilterString());

	}

	@Test
	public void testSaveFilterValueIncorrectApartmentId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctApartment() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.IVANOVA_27_330.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.IVANOVA_27_330.getId(), action.getUserPreferences().getApartmentFilter());

	}

	@Test
	public void testActionNullParents() throws Exception {

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Invalid found objects list size", action.getFoundObjects().isEmpty());

	}

	@Test
	public void testActionIncorrectParentId1() throws Exception {
		
		action.setParents(new String[] {"tt"});

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Invalid found objects list size", action.getFoundObjects().isEmpty());

	}

	@Test
	public void testActionIncorrectParentId2() throws Exception {

		action.setParents(new String[] {"0"});

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertTrue("Invalid found objects list size", action.getFoundObjects().isEmpty());

	}

	@Test
	public void testAction() throws Exception {

		action.setParents(new String[] {TestData.ADDR_IVANOVA_27.getId() + ""});

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid found objects list size", action.getFoundObjects().isEmpty());

	}

}
