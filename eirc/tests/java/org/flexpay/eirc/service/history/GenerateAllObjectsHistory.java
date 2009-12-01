package org.flexpay.eirc.service.history;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.*;
import org.flexpay.ab.service.history.*;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.history.MeasureUnitHistoryGenerator;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
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
import org.flexpay.payments.service.history.ServiceHistoryGenerator;
import org.flexpay.payments.service.history.ServiceTypeHistoryGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class GenerateAllObjectsHistory extends EircSpringBeanAwareTestCase {

	@Autowired
	private TownHistoryGenerator townHistoryGenerator;
	@Autowired
	private StreetHistoryGenerator streetHistoryGenerator;
	@Autowired
	private StreetService streetService;
	@Autowired
	private DistrictHistoryGenerator districtHistoryGenerator;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private BuildingHistoryGenerator buildingHistoryGenerator;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private ApartmentHistoryGenerator apartmentHistoryGenerator;
	@Autowired
	private ApartmentService apartmentService;
	@Autowired
	private PersonHistoryGenerator personHistoryGenerator;
	@Autowired
	private PersonService personService;
	@Autowired
	private AddressAttributeTypeHistoryGenerator addressAttributeTypeHistoryGenerator;
	@Autowired
	private AddressAttributeTypeService addressAttributeTypeService;
	@Autowired
	private StreetTypeHistoryGenerator streetTypeHistoryGenerator;
	@Autowired
	private StreetTypeService streetTypeService;
	@Autowired
	private IdentityTypeHistoryGenerator identityTypeHistoryGenerator;
	@Autowired
	private IdentityTypeService identityTypeService;
	@Autowired
	private TownTypeHistoryGenerator townTypeHistoryGenerator;
	@Autowired
	private TownTypeService townTypeService;

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
	

	private int batchSize = 200;
	private Stub<Town> townStub = new Stub<Town>(2L);

	@Test
	public void generateHistory() {

		generateTypesHistory();

		townHistoryGenerator.generateFor(new Town(townStub));

		generateTownObjectsHistory(townStub);
		generateBuildingsHistory(townStub);
		generateApartmentsHistory(townStub);
		generatePersonsHistory();

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
	}

	private void generateTownObjectsHistory(Stub<Town> town) {

		FetchRange range = new FetchRange(batchSize);
		do {
			List<Street> streets = streetService.findSimpleByTown(town, range);
			streetHistoryGenerator.generateFor(streets);
			range.nextPage();
		} while (range.hasMore());

		range = new FetchRange(batchSize);
		do {
			List<District> districts = districtService.findSimpleByTown(town, range);
			districtHistoryGenerator.generateFor(districts);
			range.nextPage();
		} while (range.hasMore());
	}

	private void generateBuildingsHistory(Stub<Town> town) {

		FetchRange range = new FetchRange(batchSize);
		do {
			List<Building> buildings = buildingService.findSimpleByTown(town, range);
			System.out.println("Fetched " + buildings.size() + " buildings");
			buildingHistoryGenerator.generateFor(buildings);
			range.nextPage();
		} while (range.hasMore());
	}

	private void generateApartmentsHistory(Stub<Town> town) {

		FetchRange range = new FetchRange(batchSize);
		do {
			List<Apartment> apartments = apartmentService.findSimpleByTown(town, range);
			System.out.println("Fetched " + apartments.size() + " apartments");
			apartmentHistoryGenerator.generateFor(apartments);
			range.nextPage();
		} while (range.hasMore());
	}

	private void generatePersonsHistory() {

		FetchRange range = new FetchRange(batchSize);
		do {
			List<Person> persons = personService.findSimple(range);
			personHistoryGenerator.generateFor(persons);
			range.nextPage();
		} while (range.hasMore());
	}

	private void generateTypesHistory() {

		identityTypeHistoryGenerator.generateFor(
				identityTypeService.getAll());

		streetTypeHistoryGenerator.generateFor(
				streetTypeService.getAll());

		addressAttributeTypeHistoryGenerator.generateFor(
				addressAttributeTypeService.getAll());

		townTypeHistoryGenerator.generateFor(
				townTypeService.getAll());
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
