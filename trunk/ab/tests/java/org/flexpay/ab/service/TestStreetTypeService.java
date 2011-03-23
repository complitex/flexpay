package org.flexpay.ab.service;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TestStreetTypeService extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("streetTypeService")
	private StreetTypeService service;
	@Autowired
	protected StreetTypeDao streetTypeDao;

	@Test
	public void testGetStreetTypes() {

		List<StreetType> streetTypes = service.getEntities();

		assertTrue("No street types", !streetTypes.isEmpty());

		// travel all references
		for (StreetType type : streetTypes) {
			Set<StreetTypeTranslation> translations = type.getTranslations();
			assertTrue("Street type without translations", !translations.isEmpty());
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

		service.create(streetType);
		streetTypeDao.delete(streetType);
	}

	@Test
	public void testFindStreetType() throws Throwable {

		assertNotNull("No type found by full name", service.findTypeByName("Улица"));
		assertNotNull("No type found by short name", service.findTypeByName("ул"));
		assertNotNull("No type found by ignore case full name", service.findTypeByName("УлиЦА"));

		assertNull("Found not usual 'xxx' type", service.findTypeByName("xxx"));
	}

	@Test (expected = FlexPayExceptionContainer.class)
	public void testSaveEmpty() throws Throwable {

		service.create(new StreetType());
	}

	@Test
	public void testUpdate() throws Throwable {

		StreetType type = service.readFull(new Stub<StreetType>(13L));
		assertNotNull("Type #13 not found", type);

		type.setTranslation(new StreetTypeTranslation("Тестовый тип"));
		service.update(type);
	}

	@Test
	public void testDeleteTranslation() throws Throwable {

		try {
			StreetType type = new StreetType();
			type.setTranslation(new StreetTypeTranslation("Тестовый тип 2"));
			type.setTranslation(new StreetTypeTranslation("Test type 2", new Language(2L)));
			service.create(type);

			assertTrue("Type was not saved", type.isNotNew());

			// delete translation
			type.setTranslation(new StreetTypeTranslation("", new Language(2L)));

			log.debug("Translations: {}", type.getTranslations());

			service.update(type);

			streetTypeDao.delete(type);
		} catch (FlexPayExceptionContainer ex) {
			log.error("Failure", ex.getFirstException());
			throw ex;
		}
	}
}
