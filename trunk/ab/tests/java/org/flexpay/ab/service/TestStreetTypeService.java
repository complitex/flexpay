package org.flexpay.ab.service;

import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestStreetTypeService extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testGetStreetTypes();
		testCreateStreetType();
	}

	public void testGetStreetTypes() {
		StreetTypeService service =
				(StreetTypeService) applicationContext.getBean("streetTypeService");

		List<StreetType> streetTypes = service.getEntities();

		assertNotNull("No streets", streetTypes);
//        assertFalse("No street types defined", streetTypes.isEmpty());
	}

	public void testCreateStreetType() throws Throwable {
		StreetTypeDao streetTypeDao =
				(StreetTypeDao) applicationContext.getBean("streetTypeDAO");

		StreetType streetType = new StreetType();
		StreetTypeTranslation typeTranslation = new StreetTypeTranslation();
		typeTranslation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		typeTranslation.setName("Test Street type");
		typeTranslation.setTranslatable(streetType);
		Set<StreetTypeTranslation> translations = new HashSet<StreetTypeTranslation>();
		translations.add(typeTranslation);
		streetType.setTranslations(translations);

		streetTypeDao.create(streetType);
		streetTypeDao.delete(streetType);
	}
}
