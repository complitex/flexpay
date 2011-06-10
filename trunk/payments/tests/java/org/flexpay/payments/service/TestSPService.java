package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.persistence.filter.EndDateFilter;
import org.flexpay.common.service.LanguageService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.TestData;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_CN;
import static org.flexpay.orgs.persistence.TestData.SRV_PROVIDER_TEST;
import static org.junit.Assert.*;
import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

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
		serviceProviderFilter.setSelected(SRV_PROVIDER_CN);
		BeginDateFilter beginDateFilter = new BeginDateFilter(new Date());
		EndDateFilter endDateFilter = new EndDateFilter(new Date());

		List<Service> result = spService.listServices(
				CollectionUtils.list(serviceProviderFilter, beginDateFilter, endDateFilter),
				new Page<Service>(1000));
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
	public void testUpdate() throws FlexPayExceptionContainer {

		ServiceDescription serviceDescription = new ServiceDescription("name");

		Service service = new Service();
		service.setBeginDate(new Date());
		service.setDescription(serviceDescription);
		service.setEndDate(new Date());
		service.setServiceType(serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA));

		service.setServiceProvider(serviceProviderService.read(SRV_PROVIDER_CN));
		spService.create(service);

		service.setServiceProvider(serviceProviderService.read(SRV_PROVIDER_TEST));
		spService.update(service);
	}

	@Test
	public void testUpdate2() throws FlexPayExceptionContainer {

		ServiceProvider spCN = serviceProviderService.read(SRV_PROVIDER_CN);
		assertNotNull("CN provider not found", spCN);
		ServiceProvider spTest = serviceProviderService.read(SRV_PROVIDER_TEST);
		assertNotNull("Test provider not found", spTest);

		Service countersRepairService = spService.readFull(TestData.SERVICE_COUNTERS_REPAIR);
		assertNotNull("Counter repair service not found", countersRepairService);
		if (countersRepairService.providerStub().sameId(spCN)) {
			countersRepairService.setServiceProvider(spTest);
		} else {
			countersRepairService.setServiceProvider(spCN);
		}

		spService.update(countersRepairService);
	}

	@Test
	public void testCreate() {

		ServiceDescription serviceDescription = new ServiceDescription("name");

		Service service = new Service();
		service.setBeginDate(new Date());
		service.setDescription(serviceDescription);
		service.setEndDate(new Date());
		service.setServiceType(serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA));

		// new service provider
		service.setServiceProvider(new ServiceProvider());
		try {
			spService.create(service);
			fail("Creating service with new service provider must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}
		service.setServiceProvider(serviceProviderService.read(SRV_PROVIDER_CN));

		// new service type
		service.setServiceType(new ServiceType());
		try {
			spService.create(service);
			fail("Creating service with new service type must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}
		service.setServiceType(serviceTypeService.read(TestData.SERVICE_TYPE_KVARTPLATA));

		// new parent service
		service.setParentService(new Service());
		try {
			spService.create(service);
			fail("Creating service with new parent service must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}

		// subservices of subservice
		service.setChildServices(CollectionUtils.set(new Service()));
		try {
			spService.create(service);
			fail("Creating service with both parent and children must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}
		service.setChildServices(Collections.<Service>emptySet());

		// same parent provider
		Service parent = new Service();
		parent.setServiceProvider(serviceProviderService.read(new Stub<ServiceProvider>(2L)));
		try {
			spService.create(service);
			fail("Creating service with parent who has different service provider must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}
		service.setParentService(null);

		// not empty default description
		serviceDescription.setName("");
		try {
			spService.create(service);
			fail("Creating service with empty default translation must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}

		serviceDescription.setName("name");
		serviceDescription.setLang(getNonDefaultLanguage());
		try {
			spService.create(service);
			fail("Creating service with no default translation must not be allowed");
		} catch (FlexPayExceptionContainer e) {
		}
	}

	@Test
	public void testDisable() {

		spService.disable(CollectionUtils.list(9999L));
	}

	@Test
	public void testFindServices() throws ParseException {

		List<Service> result;

		Long maxProviderId = (Long) uniqueResult(jpaTemplate.find("select max(id) from ServiceProvider"));
		assertNotNull("No max provider found", maxProviderId);

		// bad provider
		result = spService.findServices(new Stub<ServiceProvider>(maxProviderId + 1L), TestData.SERVICE_TYPE_KVARTPLATA, new Date());
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// bad service type
		Long maxServiceTypeId = (Long) uniqueResult(jpaTemplate.find("select max(id) from ServiceType"));
		result = spService.findServices(SRV_PROVIDER_CN, new Stub<ServiceType>(maxServiceTypeId + 1), new Date());
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// bad date
		result = spService.findServices(SRV_PROVIDER_CN, TestData.SERVICE_TYPE_KVARTPLATA, DateUtil.parseDate("2101-01-01"));
		assertNotNull("Result must not be null", result);
		assertTrue("Result must be empty on test data", result.isEmpty());

		// proper params
		result = spService.findServices(SRV_PROVIDER_CN, TestData.SERVICE_TYPE_KVARTPLATA, new Date());
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
