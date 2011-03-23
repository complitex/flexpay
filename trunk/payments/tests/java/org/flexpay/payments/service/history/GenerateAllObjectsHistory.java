package org.flexpay.payments.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.history.PersonsHistoryGenerator;
import org.flexpay.ab.service.history.TownHistoryGenerator;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.history.HistoryPacker;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.history.MeasureUnitHistoryGenerator;
import org.flexpay.orgs.persistence.*;
import org.flexpay.orgs.service.*;
import org.flexpay.orgs.service.history.CashboxHistoryGenerator;
import org.flexpay.orgs.service.history.OrganizationHistoryGenerator;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.flexpay.orgs.service.history.PaymentPointHistoryGenerator;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
	private PaymentCollectorService paymentCollectorService;
	@Autowired
	@Qualifier ("paymentCollectorHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
			PaymentCollectorDescription, PaymentCollector> paymentCollectorHistoryGenerator;

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

	@Autowired
	private HistoryPacker historyPacker;

	@Autowired
	private TownHistoryGenerator generator;
	@Autowired
	private PersonsHistoryGenerator personsHistoryGenerator;

	@Test
	public void generate() throws Exception {

		generateMeasureUnits();
		generateOrganizations();
		generateServiceOrganizations();
		generateServiceProviders();
		generateBanks();
		generatePaymentCollectors();
		generatePaymentPoints();
		generateCashboxes();
		generateServiceTypes();
		generateServices();

		generator.generateFor(new Town(2L));
		personsHistoryGenerator.generate();

//		Stub<HistoryConsumer> consumer = new Stub<HistoryConsumer>(1L);
//		List<FPFile> history = historyPacker.packHistory(consumer);
//		assertFalse("history packing failed", history.isEmpty());
//
//		System.out.println("Files:" + history);
	}

	private void generateMeasureUnits() {

		List<MeasureUnit> units = measureUnitService.find();
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

	private void generatePaymentCollectors() {

		List<PaymentCollector> organizations = paymentCollectorService.listInstances(new Page<PaymentCollector>(1000000));
		for (PaymentCollector organization : organizations) {
			paymentCollectorHistoryGenerator.generateFor(organization);
		}
	}

	private void generatePaymentPoints() {
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
		List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
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
