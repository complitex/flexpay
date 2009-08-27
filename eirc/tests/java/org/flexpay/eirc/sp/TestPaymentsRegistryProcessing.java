package org.flexpay.eirc.sp;

import static junit.framework.Assert.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.util.DateUtil;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.flexpay.payments.util.registries.RegistryFPFileFormat;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

public class TestPaymentsRegistryProcessing extends EircSpringBeanAwareTestCase {

	@Autowired
	private RegistryFPFileFormat fileFormat;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private ServiceProviderService serviceProviderService;
	@Autowired
	private RegistryRecordService registryRecordService;
	@Autowired
	private PaymentsRegistryDBGenerator paymentsRegistryDBGenerator;
	@Autowired
	@Qualifier ("registryFileParser")
	private FileParser parser;
	@Autowired
	private RegistryProcessor registryProcessor;

	@Test
	public void testGeneratePaymentsRegistryAndProcess() throws Exception {

		Organization registerOrganization = organizationService.readFull(TestData.ORG_TSZH);
		ServiceProvider serviceProvider = serviceProviderService.read(TestData.SRV_PROVIDER_CN);

		assertNotNull("Organization not found!", registerOrganization);
		assertNotNull("Service provider not found", serviceProvider);

		DateRange range1 = new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("1920-01-01"));
		Registry registry = paymentsRegistryDBGenerator.createDBRegistry(serviceProvider, registerOrganization, range1);
		assertNull("Registry generation should do nothing", registry);

		DateRange range2 = new DateRange(DateUtil.parseDate("1900-01-01"), DateUtil.parseDate("2100-12-31"));
		registry = paymentsRegistryDBGenerator.createDBRegistry(serviceProvider, registerOrganization, range2);
		assertNotNull("Registry generation failed", registry);

		Page<RegistryRecord> page = new Page<RegistryRecord>(20);
		List<RegistryRecord> records = registryRecordService.listRecords(registry,
				new ImportErrorTypeFilter(),
				new RegistryRecordStatusFilter(),
				page);
		assertEquals("Invalid records number setup", registry.getRecordsNumber().intValue(), records.size());

		FPFile file = fileFormat.generateAndAttachFile(registry);
		assertNotNull("File generation failed", file);

		List<Registry> registries = parser.parse(file);
		assertFalse("File parsing failed", registries.isEmpty());

		String hql = "select count(*) from Operation";
		Long operationsCount = (Long) DataAccessUtils.uniqueResult(hibernateTemplate.find(hql));

		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registries.get(0));
		registryProcessor.processRegistry(context);
		Long afterProcessOperationsCount = (Long) DataAccessUtils.uniqueResult(hibernateTemplate.find(hql));

		assertTrue("Processing should add new operations", operationsCount < afterProcessOperationsCount);
	}
}
