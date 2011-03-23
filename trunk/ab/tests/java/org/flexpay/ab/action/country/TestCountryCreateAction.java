package org.flexpay.ab.action.country;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.flexpay.ab.util.TestUtils.initNames;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import static org.junit.Assert.*;

public class TestCountryCreateAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryCreateAction action;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testNotSubmit() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setNames(null);
		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectNamesParameters() throws Exception {

		Map<Long, String> names = treeMap();
		names.put(564L, "test");
		Map<Long, String> shortNames = treeMap();
		shortNames.put(2L, "shorttest");

		action.setSubmitted("");
		action.setNames(names);
		action.setShortNames(shortNames);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertEquals("Invalid names map size", getLanguages().size(), action.getNames().size());
		assertEquals("Invalid shortNames map size", getLanguages().size(), action.getShortNames().size());

	}

	@Test
	public void testSubmit() throws Exception {

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setShortNames(initNames("321"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertTrue("Invalid country id", action.getCountry().getId() > 0);
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		countryDao.delete(action.getCountry());

	}

}
