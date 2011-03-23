package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.*;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.ServiceTypeNameTranslation;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.common.persistence.Stub.stub;

public class TestObjectsServices extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	private PaymentCollectorService paymentCollectorService;
	@Autowired
	private PaymentPointService paymentPointService;
	@Autowired
	private CashboxService cashboxService;
	@Autowired
	private ServiceTypeService serviceTypeService;
	@Autowired
	private MeasureUnitService unitService;
	@Autowired
	private SPService spService;

	private Language lang() {
		return ApplicationConfig.getDefaultLanguage();
	}

	private PaymentPoint newPoint() throws FlexPayExceptionContainer {

		PaymentPoint point = new PaymentPoint();
		point.setCollector(createCollector(createOrganization()));
		point.setAddress("TEST_POINT_ADDRESS");		
		point.setName(new PaymentPointName("TEST-POINT", lang()));
		return paymentPointService.create(point);
	}

	private Organization createOrganization() throws FlexPayExceptionContainer {
		Organization organization = new Organization();
		organization.setName(new OrganizationName("TEST-ORG", lang()));
		organization.setDescription(new OrganizationDescription("TEST-ORG", lang()));
		organization.setJuridicalAddress("TEST-ORG-JURIDICAL-ADDRESS");
		organization.setPostalAddress("TEST-ORG-POSTAL-ADDRESS");
		organization.setIndividualTaxNumber("123123");
		organization.setKpp("123123");
		return organizationService.create(organization);
	}

	private ServiceProvider createProvider(Organization organization) throws FlexPayExceptionContainer {

		ServiceProvider org = new ServiceProvider();
		org.setOrganization(organization);
		org.setDescription(new ServiceProviderDescription("TEST", lang()));
		return serviceProviderService.create(org);
	}

	private PaymentCollector createCollector(Organization organization) throws FlexPayExceptionContainer {

		PaymentCollector org = new PaymentCollector();
		org.setOrganization(organization);
		org.setDescription(new PaymentCollectorDescription("TEST", lang()));
		return paymentCollectorService.create(org);
	}

	private void delete(PaymentPoint point) {

		PaymentCollector collector = point.getCollector();
		paymentPointService.delete(point);

		Stub<Organization> organizationStub = collector.getOrganizationStub();
		paymentCollectorService.delete(collector);

		organizationService.delete(organizationStub);
	}

	private void delete(ServiceType type) {
		serviceTypeService.delete(type);
	}

	private void delete(ServiceProvider provider) {
		serviceProviderService.delete(provider);
	}

	private void delete(Organization organization) {
		organizationService.delete(stub(organization));
	}

	private ServiceType newServiceType(int code) throws FlexPayExceptionContainer {
		ServiceType type = new ServiceType();
		type.setCode(code);
		type.setTypeName(new ServiceTypeNameTranslation("TEST-TYPE", "TEST-TYPE-DESC", lang()));
		return serviceTypeService.create(type);
	}

	private MeasureUnit newMeasureUnit() throws FlexPayExceptionContainer {
		MeasureUnit unit = new MeasureUnit();
		unit.setName(new MeasureUnitName("TEST-UNIT", lang()));
		return unitService.create(unit);
	}

	private void delete(MeasureUnit unit) {
		unitService.delete(unit);
	}

	@Test
	public void testUpdateCashbox() throws FlexPayExceptionContainer {

		PaymentPoint point = newPoint();
		Cashbox cashbox = new Cashbox();
		cashbox.setPaymentPoint(point);
		cashbox.setName(new CashboxNameTranslation("TEST-CASHBOX", lang()));
		cashboxService.create(cashbox);

		cashbox.setName(new CashboxNameTranslation("TEST-CASHBOX-UPDATE", lang()));
		cashboxService.update(cashbox);

		cashboxService.delete(cashbox);
		delete(point);
	}

	@Test
	public void testUpdateServiceType() throws FlexPayExceptionContainer {

		ServiceType type = newServiceType(123);

		type.setCode(123456);
		type.setTypeName(new ServiceTypeNameTranslation("TEST-TYPE-UPDATE", "DESCR", lang()));
		serviceTypeService.update(type);

		delete(type);
	}

	@Test
	public void testUpdateService() throws FlexPayExceptionContainer {

		Service service = new Service();
		service.setExternalCode("#21");

		ServiceType type1 = newServiceType(21);
		service.setServiceType(type1);

		Organization org = createOrganization();
		ServiceProvider provider1 = createProvider(org);
		service.setServiceProvider(provider1);

		MeasureUnit unit1 = newMeasureUnit();
		service.setMeasureUnit(unit1);

		service.setBeginDate(DateUtil.parseBeginDate("2009/01/01"));
		service.setEndDate(DateUtil.parseEndDate("2009/12/31"));
		service.setDescription(new ServiceDescription("TEST-SRV-DESC", lang()));

		spService.create(service);

		service.setExternalCode("#22");
		ServiceType type2 = newServiceType(22);
		service.setServiceType(type2);

		ServiceProvider provider2 = createProvider(org);
		service.setServiceProvider(provider2);

		MeasureUnit unit2 = newMeasureUnit();
		service.setMeasureUnit(unit2);

		service.setBeginDate(DateUtil.parseBeginDate("2010/01/01"));
		service.setEndDate(DateUtil.parseEndDate("2010/12/31"));
		spService.update(service);

		spService.delete(service);
		delete(unit1);
		delete(unit2);
		delete(provider1);
		delete(provider2);
		delete(type1);
		delete(type2);
		delete(org);
	}
}
