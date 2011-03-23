package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.filter.FilterAjaxAction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TestStreetFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetFilterAjaxAction action;
	@Autowired
	private StreetService streetService;

	@Test
	public void testPrerequestNullStreetId() throws Exception {

		action.setPreRequest(true);

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestIncorrectStreetId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestDefunctStreet() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.IVANOVA.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.IVANOVA.getId(), action.getUserPreferences().getStreetFilter());

		Street street = streetService.readFull(TestData.IVANOVA);
		String string = action.getTranslation(street.getCurrentType().getTranslations()).getShortName() + " " +
						action.getTranslationName(street.getCurrentName().getTranslations());
		assertEquals("Invalid filter string value", string, action.getFilterString());

	}

	@Test
	public void testSaveFilterValueIncorrectStreetId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctStreet() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.IVANOVA.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.IVANOVA.getId(), action.getUserPreferences().getStreetFilter());

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

		action.setParents(new String[] {TestData.TOWN_NSK.getId() + ""});
		action.setQ("ива");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid found objects list size. Must be 1", 1, action.getFoundObjects().size());

	}

}
