package org.flexpay.common.dao;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.DataSourceDescription;

public class TestCorrectionsDao extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testGetLanguages();
	}

	public void testGetLanguages() {
		CorrectionsDao correctionsDao =
				(CorrectionsDao) applicationContext.getBean("correctionsDao");

		DataSourceDescription dsd = new DataSourceDescription();
		dsd.setId(-4L);
		Language language = (Language) correctionsDao.findCorrection(
				"hbz", 10, Language.class, dsd);

		assertNull("Found not existing object", language);
	}
}
