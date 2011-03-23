package org.flexpay.payments.export.util;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.Assert.*;

public class TestPaymentsRegistryDBGenerator extends PaymentsSpringBeanAwareTestCase {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	private RegistryRecordService registryRecordService;
	@Autowired
	private PaymentsRegistryDBGenerator paymentsRegistryDBGenerator;

	@Test
	public void testCreateDBRegistry() throws Exception {

		Organization registerOrganization = organizationService.readFull(TestData.ORG_TSZH);
		ServiceProvider serviceProvider = serviceProviderService.read(TestData.SRV_PROVIDER_CN);

		assertNotNull("Service provider not found", serviceProvider);

		DateRange range1 = new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("1920-01-01"));
		Registry registry = paymentsRegistryDBGenerator.createRegistry(serviceProvider, registerOrganization, range1);
		assertNull("Registry generation should do nothing", registry);

		DateRange range2 = new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("2100-12-31"));
		registry = paymentsRegistryDBGenerator.createRegistry(serviceProvider, registerOrganization, range2);
		assertNotNull("Registry generation failed", registry);

		Page<RegistryRecord> page = new Page<RegistryRecord>(20);
		List<RegistryRecord> records = registryRecordService.listRecords(registry,
				new ImportErrorTypeFilter(),
				new RegistryRecordStatusFilter(),
				page);
		assertEquals("Invalid records number setup", registry.getRecordsNumber().intValue(), records.size());
	}
}
