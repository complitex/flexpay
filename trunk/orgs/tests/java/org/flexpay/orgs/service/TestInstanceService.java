package org.flexpay.orgs.service;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.test.OrgsSpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertNotNull;
import static org.flexpay.common.persistence.Stub.stub;

public class TestInstanceService extends OrgsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceOrganizationService serviceOrganizationService;
	@Autowired
	private BankService bankService;
	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	private PaymentCollectorService paymentCollectorService;
	@Autowired
	private PaymentPointService paymentPointService;

	private Organization organization;

	private Language lang() {
		return ApplicationConfig.getDefaultLanguage();
	}

	@Test
	public void testEditServiceOrganization() throws Exception {
		ServiceOrganization org = ServiceOrganization.newInstance();
		org.setOrganization(organization);
		org.setDescription(new ServiceOrganizationDescription("TEST", lang()));
		serviceOrganizationService.create(org);

		org.setDescription(new ServiceOrganizationDescription("TEST-UPDATE", lang()));
		serviceOrganizationService.update(org);

		serviceOrganizationService.delete(org);
	}

	@Test
	public void testEditBank() throws Exception {
		Bank org = new Bank();
		org.setOrganization(organization);
		org.setBankIdentifierCode("123");
		org.setCorrespondingAccount("1231123123");
		org.setDescription(new BankDescription("Test", lang()));
		bankService.create(org);

		org.setCorrespondingAccount("312213");
		org.setBankIdentifierCode("3421");
		org.setDescription(new BankDescription("TEST-UPDATE", lang()));
		bankService.update(org);

		bankService.delete(org);
	}

	@Test
	public void testEditServiceProvider() throws Exception {
		ServiceProvider org = new ServiceProvider();
		org.setOrganization(organization);
		org.setDescription(new ServiceProviderDescription("TEST", lang()));
		serviceProviderService.create(org);

		org.setDescription(new ServiceProviderDescription("TEST-UPDATE", lang()));
		serviceProviderService.update(org);

		serviceProviderService.delete(org);
	}

	@Test
	public void testEditPaymentCollector() throws Exception {
		PaymentCollector org = new PaymentCollector();
		org.setOrganization(organization);
		org.setDescription(new PaymentCollectorDescription("TEST", lang()));
		paymentCollectorService.create(org);

		org.setDescription(new PaymentCollectorDescription("TEST-UPDATE", lang()));
		paymentCollectorService.update(org);

		paymentCollectorService.delete(org);
	}

	@Test
	public void testEditPaymentPoint() throws Exception {
		PaymentCollector org = new PaymentCollector();
		org.setOrganization(organization);
		org.setDescription(new PaymentCollectorDescription("TEST", lang()));
		paymentCollectorService.create(org);

		PaymentPoint point = new PaymentPoint();
		point.setCollector(org);
		point.setAddress("TEST_POINT_ADDRESS");
		point.setName(new PaymentPointName("TEST-POINT", lang()));
		paymentPointService.create(point);

		point.setAddress("ADDR");
		point.setName(new PaymentPointName("TEST-POINT-UPDATE", lang()));
		paymentPointService.update(point);

		paymentPointService.delete(point);
		paymentCollectorService.delete(org);
	}

	@Test
	public void testEditPaymentPoint2() throws Exception {
		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(1L));
		assertNotNull("Payment point did not find", paymentPoint);
		paymentPoint.setTradingDayProcessInstanceId(1000L);
		paymentPointService.update(paymentPoint);
	}

	@Before
	public void createOrganization() throws Exception {

		organization = new Organization();
		organization.setName(new OrganizationName("TEST-ORG", lang()));
		organization.setDescription(new OrganizationDescription("TEST-ORG", lang()));
		organization.setJuridicalAddress("TEST-ORG-JURIDICAL-ADDRESS");
		organization.setPostalAddress("TEST-ORG-POSTAL-ADDRESS");
		organization.setIndividualTaxNumber("123123");
		organization.setKpp("123123");
		organizationService.create(organization);
	}

	@After
	public void deleteOrganization() {

		organizationService.delete(stub(organization));
	}
}
