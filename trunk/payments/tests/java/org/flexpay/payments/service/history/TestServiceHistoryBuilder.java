package org.flexpay.payments.service.history;

import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.service.history.MeasureUnitHistoryGenerator;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.history.OrganizationHistoryGenerator;
import org.flexpay.orgs.service.history.OrganizationInstanceHistoryGenerator;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.TestData;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServiceHistoryBuilder extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private ServiceHistoryBuilder historyBuilder;
	@Autowired
	private SPService spService;
	@Autowired
	private ServiceHistoryGenerator serviceHistoryGenerator;

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private OrganizationHistoryGenerator organizationHistoryGenerator;
	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	@Qualifier ("serviceProviderHistoryGenerator")
	private OrganizationInstanceHistoryGenerator<
				ServiceProviderDescription, ServiceProvider> serviceProviderHistoryGenerator;
	@Autowired
	private MeasureUnitService measureUnitService;
	@Autowired
	private MeasureUnitHistoryGenerator measureUnitHistoryGenerator;
	@Autowired
	private ServiceTypeService serviceTypeService;
	@Autowired
	private ServiceTypeHistoryGenerator serviceTypeHistoryGenerator;

	@Test
	public void testPatchService() {

		Service service = spService.readFull(TestData.SERVICE_TERRITORY_CLEANUP);
		assertNotNull("Territory cleanup service not found", service);

		generateReferencesHistory(service);

		Diff diff = historyBuilder.diff(null, service);
		Service copy = new Service();

		historyBuilder.patch(copy, diff);

		assertEquals("Invalid provider patch", service.providerStub(), copy.providerStub());
		assertEquals("Invalid type patch", service.serviceTypeStub(), copy.serviceTypeStub());
		assertEquals("Invalid code patch", service.getExternalCode(), copy.getExternalCode());
		assertNotNull("No begin date", copy.getBeginDate());
		assertEquals("Invalid begin date patch", service.getBeginDate(), copy.getBeginDate());
		assertEquals("Invalid end date patch", service.getEndDate(), copy.getEndDate());
		assertEquals("Invalid measure unit patch", service.getMeasureUnit(), copy.getMeasureUnit());
		assertEquals("Invalid parent patch", service.getParentService(), copy.getParentService());
	}

	private void generateReferencesHistory(Service service) {

		ServiceProvider provider = serviceProviderService.read(service.providerStub());
		assertNotNull("No provider found", provider);
		Organization organization = organizationService.readFull(provider.getOrganizationStub());
		assertNotNull("No organization found", organization);
		organizationHistoryGenerator.generateFor(organization);
		serviceProviderHistoryGenerator.generateFor(provider);
		if (service.hasMeasureUnit()) {
			MeasureUnit measureUnit = measureUnitService.readFull(service.measureUnitStub());
			assertNotNull("Measure unit not found: " + service.measureUnitStub(), measureUnit);
			measureUnitHistoryGenerator.generateFor(measureUnit);
		}
		ServiceType type = serviceTypeService.read(service.serviceTypeStub());
		assertNotNull("Type not found", type);
		serviceTypeHistoryGenerator.generateFor(type);

		if (service.isSubService()) {
			Service parent = spService.readFull(service.parentServiceStub());
			assertNotNull("Parent service not found: " + service.parentServiceStub(), parent);
			generateReferencesHistory(parent);
			serviceHistoryGenerator.generateFor(parent);
		}
	}
}
