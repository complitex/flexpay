package org.flexpay.payments.service.history;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.history.MeasureUnitHistoryGenerator;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.*;
import org.flexpay.orgs.service.history.OrganizationHistoryGenerator;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.flexpay.orgs.service.history.PaymentPointHistoryGenerator;
import org.flexpay.payments.service.CashboxService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.flexpay.payments.persistence.Cashbox;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.junit.Test;

import java.util.List;

public class GenerateAllObjectsHistory extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private OrganizationHistoryGenerator organizationHistoryGenerator;

	@Autowired
	private MeasureUnitService measureUnitService;
	@Autowired
	private MeasureUnitHistoryGenerator measureUnitHistoryGenerator;

	@Autowired
	private ServiceOrganizationService serviceOrganizationService;
	@Autowired
	@Qualifier ("serviceOrganizationHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
			ServiceOrganizationDescription, ServiceOrganization> serviceOrganizationHistoryGenerator;

	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	@Qualifier ("serviceProviderHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
			ServiceProviderDescription, ServiceProvider> serviceProviderHistoryGenerator;

	@Autowired
	private BankService bankService;
	@Autowired
	@Qualifier ("bankHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
			BankDescription, Bank> bankHistoryGenerator;

	@Autowired
	private PaymentsCollectorService paymentsCollectorService;
	@Autowired
	@Qualifier ("paymentsCollectorHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
			PaymentsCollectorDescription, PaymentsCollector> paymentsCollectorHistoryGenerator;

	@Autowired
	private PaymentPointService paymentPointService;
	@Autowired
	private PaymentPointHistoryGenerator paymentPointHistoryGenerator;

	@Autowired
	private CashboxService cashboxService;
	@Autowired
	private CashboxHistoryGenerator cashboxHistoryGenerator;

	@Autowired
	private ServiceTypeService serviceTypeService;
	@Autowired
	private ServiceTypeHistoryGenerator serviceTypeHistoryGenerator;

	@Autowired
	private SPService spService;
	@Autowired
	private ServiceHistoryGenerator serviceHistoryGenerator;

	@Test
	public void generate() {

		generateMeasureUnits();
		generateOrganizations();
		generateServiceOrganizations();
		generateServiceProviders();
		generateBanks();
		generatePaymentsCollectors();
		generatePaimentPoints();
		generateCashboxes();
		generateServiceTypes();
		generateServices();
	}

	private void generateMeasureUnits() {

		List<MeasureUnit> units = measureUnitService.listUnits();
		for (MeasureUnit unit : units) {
			measureUnitHistoryGenerator.generateFor(unit);
		}
	}

	private void generateOrganizations() {

		List<Organization> organizations = organizationService.listOrganizations(new Page<Organization>(1000000));
		for (Organization organization : organizations) {
			organizationHistoryGenerator.generateFor(organization);
		}
	}

	private void generateServiceOrganizations() {

		List<ServiceOrganization> organizations = serviceOrganizationService.listServiceOrganizations();
		for (ServiceOrganization organization : organizations) {
			serviceOrganizationHistoryGenerator.generateFor(organization);
		}
	}

	private void generateServiceProviders() {

		List<ServiceProvider> organizations = serviceProviderService.listInstances(new Page<ServiceProvider>(1000000));
		for (ServiceProvider organization : organizations) {
			serviceProviderHistoryGenerator.generateFor(organization);
		}
	}

	private void generateBanks() {

		List<Bank> organizations = bankService.listInstances(new Page<Bank>(1000000));
		for (Bank organization : organizations) {
			bankHistoryGenerator.generateFor(organization);
		}
	}

	private void generatePaymentsCollectors() {

		List<PaymentsCollector> organizations = paymentsCollectorService.listInstances(new Page<PaymentsCollector>(1000000));
		for (PaymentsCollector organization : organizations) {
			paymentsCollectorHistoryGenerator.generateFor(organization);
		}
	}

	private void generatePaimentPoints() {
		List<PaymentPoint> paymentPoints = paymentPointService.findAll();
		for (PaymentPoint point : paymentPoints) {
			paymentPointHistoryGenerator.generateFor(point);
		}
	}

	private void generateCashboxes() {
		List<Cashbox> cashboxes = cashboxService.findObjects(new Page<Cashbox>(1000000));
		for (Cashbox cashbox : cashboxes) {
			cashboxHistoryGenerator.generateFor(cashbox);
		}
	}

	private void generateServiceTypes() {
		List<ServiceType> serviceTypes = serviceTypeService.listAllServiceTypes();
		for (ServiceType type : serviceTypes) {
			serviceTypeHistoryGenerator.generateFor(type);
		}
	}

	private void generateServices() {
		List<Service> services = spService.listAllServices();
		for (Service service : services) {
			serviceHistoryGenerator.generateFor(service);
		}
	}
}
