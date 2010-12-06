package org.flexpay.ab.action.filter;

import org.flexpay.common.actions.filter.FilterAjaxAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountryStub;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCountryFilterAjaxAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryFilterAjaxAction action;
	@Autowired
	private CountryService countryService;

	@Test
	public void testPrerequestIncorrectCountryId() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid filterValue parameter", getDefaultCountryStub().getId() + "", action.getFilterValue());

		String countryName = action.getTranslationName(countryService.readFull(getDefaultCountryStub()).getTranslations());
		assertEquals("Invalid filterString parameter", countryName, action.getFilterString());
		assertEquals("Invalid country id in user preferences in session", getDefaultCountryStub().getId(), action.getUserPreferences().getCountryFilter());

	}

	@Test
	public void testPrerequestDefunctCountry() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue("122332");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testPrerequest() throws Exception {

		action.setPreRequest(true);
		action.setFilterValue(TestData.COUNTRY_USA.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.PREREQUEST_RESPONSE, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.COUNTRY_USA.getId(), action.getUserPreferences().getCountryFilter());

	}

	@Test
	public void testSaveFilterValueIncorrectCountryId() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("ttt");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testSaveFilterValueDefunctCountry() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue("1223322");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", getDefaultCountryStub().getId(), action.getUserPreferences().getCountryFilter());

	}

	@Test
	public void testSaveFilterValue() throws Exception {

		action.setSaveFilterValue(true);
		action.setFilterValue(TestData.COUNTRY_USA.getId() + "");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid country id in user preferences in session", TestData.COUNTRY_USA.getId(), action.getUserPreferences().getCountryFilter());

	}

	@Test
	public void testAction1() throws Exception {

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid found objects list size", action.getFoundObjects().isEmpty());

	}

	@Test
	public void testAction2() throws Exception {

		action.setQ("rus");

		assertEquals("Invalid action result", FilterAjaxAction.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertEquals("Invalid found objects list size. Must be 1", 1, action.getFoundObjects().size());

	}

}
