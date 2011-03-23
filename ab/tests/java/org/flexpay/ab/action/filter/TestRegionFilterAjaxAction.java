package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.filter.FilterAjaxAction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultRegionStub;
import static org.junit.Assert.*;

public class TestRegionFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionFilterAjaxAction action;
	@Autowired
	private RegionService regionService;

	@Test
	public void testPrerequestNullRegionId() throws Exception {

		action.setPreRequest(true);

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid filterValue parameter", getDefaultRegionStub().getId() + "", action.getFilterValue());

	}

	@Test
	public void testPrerequestIncorrectRegionId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid filterValue parameter", getDefaultRegionStub().getId() + "", action.getFilterValue());

	}

	@Test
	public void testPrerequestDefunctRegion() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.REGION_TSK.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.REGION_TSK.getId(), action.getUserPreferences().getRegionFilter());

		Region region = regionService.readFull(TestData.REGION_TSK);
		assertEquals("Invalid filter string value", action.getTranslationName(region.getCurrentName().getTranslations()), action.getFilterString());

	}

	@Test
	public void testSaveFilterValueIncorrectRegionId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctRegion() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid region id in user preferences in session", getDefaultRegionStub().getId(), action.getUserPreferences().getRegionFilter());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.REGION_TSK.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.REGION_TSK.getId(), action.getUserPreferences().getRegionFilter());

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

		action.setParents(new String[] {TestData.COUNTRY_RUS.getId() + ""});
		action.setQ("ново");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid found objects list size. Must be 1", 1, action.getFoundObjects().size());

	}

}
