package org.flexpay.ab.action.country;

import org.flexpay.ab.actions.country.CountryCreateAction;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TestCountryCreateAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryCreateAction action;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testNotSubmit() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testSubmit() throws Exception {

		action.setSubmitted("");

		Map<Long, String> names = treeMap();
		Map<Long, String> shortNames = treeMap();

		for (Language lang : ApplicationConfig.getLanguages()) {
			names.put(lang.getId(), "123");
			shortNames.put(lang.getId(), "321");
		}

		action.setNames(names);
		action.setShortNames(shortNames);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		countryDao.delete(action.getCountry());

	}

}
