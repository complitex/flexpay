package org.flexpay.ab.action.filter;

import org.flexpay.common.actions.filter.FilterAjaxAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTownStub;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownFilterAjaxAction action;
	@Autowired
	private TownService townService;

	@Test
	public void testPrerequestNullTownId() throws Exception {

		action.setPreRequest(true);

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid filterValue parameter", getDefaultTownStub().getId() + "", action.getFilterValue());

	}

	@Test
	public void testPrerequestIncorrectTownId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid filterValue parameter", getDefaultTownStub().getId() + "", action.getFilterValue());

	}

	@Test
	public void testPrerequestDefunctTown() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.TOWN_BERDSK.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.TOWN_BERDSK.getId(), action.getUserPreferences().getTownFilter());

		Town town = townService.readFull(TestData.TOWN_BERDSK);
		assertEquals("Invalid filter string value", action.getTranslationName(town.getCurrentName().getTranslations()), action.getFilterString());

	}

	@Test
	public void testSaveFilterValueIncorrectTownId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctTown() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid town id in user preferences in session", getDefaultTownStub().getId(), action.getUserPreferences().getTownFilter());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.TOWN_BERDSK.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.TOWN_BERDSK.getId(), action.getUserPreferences().getTownFilter());

	}

	@Test
	public void testActionNullParents() throws Exception {

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid found objects list size", action.getFoundObjects().isEmpty());

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

		action.setParents(new String[] {TestData.REGION_NSK.getId() + ""});
		action.setQ("вос");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid found objects list size. Must be 1", 1, action.getFoundObjects().size());

	}

}
