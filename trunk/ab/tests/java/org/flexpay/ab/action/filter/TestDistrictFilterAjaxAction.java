package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.filter.FilterAjaxAction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TestDistrictFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictFilterAjaxAction action;
	@Autowired
	private DistrictService districtService;

	@Test
	public void testPrerequestNullDistrictId() throws Exception {

		action.setPreRequest(true);

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestIncorrectDistrictId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequestDefunctDistrict() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid filterString parameter", "", action.getFilterString());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.DISTRICT_SOVETSKIY.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.DISTRICT_SOVETSKIY.getId(), action.getUserPreferences().getDistrictFilter());

		District district = districtService.readFull(TestData.DISTRICT_SOVETSKIY);
		assertEquals("Invalid filter string value", action.getTranslationName(district.getCurrentName().getTranslations()), action.getFilterString());

	}

	@Test
	public void testSaveFilterValueIncorrectDistrictId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctDistrict() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.DISTRICT_SOVETSKIY.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.DISTRICT_SOVETSKIY.getId(), action.getUserPreferences().getDistrictFilter());

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
		action.setQ("совет");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid found objects list size. Must be 1", 1, action.getFoundObjects().size());

	}

}
