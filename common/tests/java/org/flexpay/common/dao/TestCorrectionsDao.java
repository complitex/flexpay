package org.flexpay.common.dao;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCorrectionsDao extends SpringBeanAwareTestCase {

	@Autowired
	protected CorrectionsDao correctionsDao;

	@Test
	public void testGetLanguage() {

		DataSourceDescription dsd = new DataSourceDescription(-4L);
		Stub<Language> language = correctionsDao.findCorrection("hbz", 10, Language.class, dsd);

		assertNull("Found not existing object", language);
	}
}