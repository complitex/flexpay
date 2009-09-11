package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.util.config.ApplicationConfig;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestServiceTypeService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private LanguageService languageService;

	@Test
	public void testDisable() {
		serviceTypeService.disable(CollectionUtils.set(9999L));
	}

	@Test
	public void testListServiceTypes() {

		int pageSize = 10;
		List<ServiceType> result = serviceTypeService.listServiceTypes(new Page<ServiceType>(pageSize));
		assertNotNull("Result must no t be null", result);
		assertTrue("Result size is bigger than page size", result.size() <= pageSize);

		result = serviceTypeService.listServiceTypes(new Page<ServiceType>(9999));
		assertNotNull("Result must no t be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testListAllServiceTypes() {

		List<ServiceType> result = serviceTypeService.listAllServiceTypes();
		assertNotNull("Result must no t be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testCreate() {

		ServiceTypeNameTranslation translation = new ServiceTypeNameTranslation();
		translation.setName("name1");
		translation.setDescription("name2");
		translation.setLang(getDefaultLanguage());

		ServiceType serviceType = new ServiceType();
		serviceType.setTypeName(translation);

		// no code
		try {
			serviceTypeService.create(serviceType);
			fail("Creating type with no code must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		// check default translation exists
		serviceType.setCode(9999);
		translation.setLang(getNonDefaultLanguage());

		try {
			serviceTypeService.create(serviceType);
			fail("Creating type with no default translation must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		// blank name
		translation.setLang(getDefaultLanguage());
		translation.setName("");
		try {
			serviceTypeService.create(serviceType);
			fail("Creating type with blank name must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		// blank description
		translation.setName("name");
		translation.setDescription("");
		try {
			serviceTypeService.create(serviceType);
			fail("Creating type with blank description must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		// existent code
		serviceType.setCode(1);
		try {
			serviceTypeService.create(serviceType);
			fail("Creating type with existent code must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
	}

	@Test
	public void testGetServiceType() {

		ServiceType existentType = serviceTypeService.getServiceType(1);
		assertNotNull("Service type must not be null for reserved code", existentType);

		try {
			serviceTypeService.getServiceType(9999);
			fail("Reading service by incorrect code must rise an exception");
		} catch (IllegalArgumentException e) {}
	}

	private Language getNonDefaultLanguage() {

		List<Language> languages = languageService.getLanguages();
		for (Language language : languages) {
			if (!language.isDefault()) {
				return language;
			}
		}

		return null;
	}

	private Language getDefaultLanguage() {
		return ApplicationConfig.getDefaultLanguage();
	}
}