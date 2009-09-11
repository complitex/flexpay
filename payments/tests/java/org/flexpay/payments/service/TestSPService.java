package org.flexpay.payments.service;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.LanguageService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.TestData;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.text.ParseException;

public class TestSPService extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private SPService spService;

	@Autowired
	private ServiceProviderService serviceProviderService;

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private LanguageService languageService;
	

	@Test
	public void testListServices() {

		ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();
		serviceProviderFilter.setSelected(org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN);
		BeginDateFilter beginDateFilter = new BeginDateFilter();
		beginDateFilter.setDate(new Date());
		EndDateFilter endDateFilter = new EndDateFilter();
		endDateFilter.setDate(new Date());

		List<Service> result = spService.listServices(CollectionUtils.list(serviceProviderFilter, beginDateFilter, endDateFilter), new Page<Service>(1000));
		assertNotNull("Result must not be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
	}

	@Test
	public void testListAllServices() {

		List<Service> result = spService.listAllServices();
		assertNotNull("Result must not be null", result);
		assertFalse("Result must not be empty", result.isEmpty());
	}

	@Test
	public void testCreate() {

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setLang(ApplicationConfig.getDefaultLanguage());
		serviceDescription.setName("name");

		Service service = new Service();
		service.setBeginDate(new Date());
		service.setChildServices(Collections.<Service>emptySet());
		service.setDescription(serviceDescription);
		service.setEndDate(new Date());		
		service.setServiceType(serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA));

		// new service provider
		service.setServiceProvider(new ServiceProvider());
		try {
			spService.create(service);
			fail("Creating service with new service provider must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
		service.setServiceProvider(serviceProviderService.read(org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN));

		// new service type
		service.setServiceType(new ServiceType());
		try {
			spService.create(service);
			fail("Creating service with new service type must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
		service.setServiceType(serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA));

		// new parent service
		service.setParentService(new Service());
		try {
			spService.create(service);
			fail("Creating service with new parent service must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		// subservices of subservice
		service.setChildServices(CollectionUtils.set(new Service()));
		try {
			spService.create(service);
			fail("Creating service with both parent and children must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
		service.setChildServices(Collections.<Service>emptySet());

		// same parent provider
		Service parent = new Service();
		parent.setServiceProvider(serviceProviderService.read(new Stub<ServiceProvider>(2L)));
		try {
			spService.create(service);
			fail("Creating service with parent who has different service provider must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
		service.setParentService(null);

		// not empty default description
		serviceDescription.setName("");
		try {
			spService.create(service);
			fail("Creating service with empty default translation must not be allowed");
		} catch (FlexPayExceptionContainer e) {}

		serviceDescription.setName("name");
		serviceDescription.setLang(getNonDefaultLanguage());
		try {
			spService.create(service);
			fail("Creating service with no default translation must not be allowed");
		} catch (FlexPayExceptionContainer e) {}
	}

	@Test
	public void testDisable() {

		spService.disable(CollectionUtils.list(9999L));
	}

	@Test
	public void testFindServices() throws ParseException {

		List<Service> result;

		// bad provider
		result = spService.findServices(new Stub<ServiceProvider>(2L), TestData.SERVICE_TYPE_KVARTPLATA, new Date());
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// bad service type
		result = spService.findServices(org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN, new Stub<ServiceType>(999L), new Date());
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// bad date
		result = spService.findServices(org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN, TestData.SERVICE_TYPE_KVARTPLATA, DateUtil.parseDate("2101-01-01"));
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// proper params
		result = spService.findServices(org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN, TestData.SERVICE_TYPE_KVARTPLATA, new Date());
		assertNotNull("Result must not be null", result);
		assertFalse("Result must not be empty on test data", result.isEmpty());
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
}
