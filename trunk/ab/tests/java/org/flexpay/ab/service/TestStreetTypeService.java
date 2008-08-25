package org.flexpay.ab.service;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestStreetTypeService extends SpringBeanAwareTestCase {

	private StreetTypeService service;
	@Autowired
	protected StreetTypeDao streetTypeDao;

	@Autowired
	public void setService(@Qualifier ("streetTypeService") StreetTypeService service) {
		this.service = service;
	}

	@Test
	@NotTransactional
	public void testGetStreetTypes() {

		List<StreetType> streetTypes = service.getEntities();

		assertTrue("No street types", streetTypes.size() > 0);

		// travel all references
		for (StreetType type : streetTypes) {
			Set<StreetTypeTranslation> translations = type.getTranslations();
			assertTrue("Street type without translations", translations.size() > 0);
			for (StreetTypeTranslation translation : translations) {
				assertTrue("Empty translation found", StringUtils.isNotBlank(translation.getName()));
			}
		}
	}

	@Test
	public void testCreateStreetType() throws Throwable {

		StreetType streetType = new StreetType();
		StreetTypeTranslation typeTranslation = new StreetTypeTranslation();
		typeTranslation.setLang(ApplicationConfig.getDefaultLanguage());
		typeTranslation.setName("Test Street type");
		typeTranslation.setTranslatable(streetType);
		Set<StreetTypeTranslation> translations = new HashSet<StreetTypeTranslation>();
		translations.add(typeTranslation);
		streetType.setTranslations(translations);

		streetTypeDao.create(streetType);
		streetTypeDao.delete(streetType);
	}

	@Test
	public void testFindStreetType() throws Throwable {
		assertNotNull("No type found by full name", service.findTypeByName("Улица"));
		assertNotNull("No type found by short name", service.findTypeByName("ул"));
		assertNotNull("No type found by ignore case full name", service.findTypeByName("УлиЦА"));

		assertNull("Found not usual 'xxx' type", service.findTypeByName("xxx"));
	}
}
