package org.flexpay.eirc.sp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.process.registry.StartRegistryProcessingActionHandler;
import org.flexpay.eirc.service.exchange.RegistryProcessor;
import org.flexpay.eirc.test.EircSpringBeanAwareTestCase;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.TestData;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.flexpay.payments.util.registries.RegistryFPFileFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.DateUtil.parseDate;
import static org.junit.Assert.assertFalse;

public class TestPaymentsRegistryProcessing2 extends EircSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("paymentsRegistryFPFileFormat")
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
	@Autowired
	private RegistryService registryService;
	 @Autowired
	private ProcessManager testProcessManager;

	@Test
	public void testGeneratePaymentsRegistryAndProcess() throws Exception {
		testProcessManager.deployProcessDefinition("ProcessingDBRegistryProcess", true);

		Organization registerOrganization = organizationService.readFull(TestData.ORG_TSZH);
		ServiceProvider serviceProvider = serviceProviderService.read(TestData.SRV_PROVIDER_CN);

		assertNotNull("Organization not found!", registerOrganization);
		assertNotNull("Service provider not found", serviceProvider);

		DateRange range1 = new DateRange(parseDate("1900-01-01"), parseDate("1920-01-01"));
		Registry registry = paymentsRegistryDBGenerator.createRegistry(serviceProvider, registerOrganization, range1);
		assertNull("Registry generation should do nothing", registry);

		DateRange range2 = new DateRange(parseDate("1900-01-01"), parseDate("2100-12-31"));
		registry = paymentsRegistryDBGenerator.createRegistry(serviceProvider, registerOrganization, range2);
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

		Registry newRegistry = registries.get(0);
		Map<Serializable, Serializable> parameters = new HashMap<Serializable, Serializable>();
		parameters.put(StartRegistryProcessingActionHandler.REGISTRY_ID, newRegistry.getId());
		long processId = testProcessManager.createProcess("ProcessingDBRegistryProcess", parameters);
		assertTrue("Process can not created", processId > 0);
		org.flexpay.common.process.Process process = testProcessManager.getProcessInstanceInfo(processId);
		assertNotNull("Process did not find", process);
		do {
			log.debug("Wait completed process {}", processId);
			Thread.sleep(100);
			process = testProcessManager.getProcessInstanceInfo(processId);
		} while(!process.getProcessState().isCompleted());
		newRegistry = registryService.read(Stub.stub(newRegistry));
		assertNotNull("Processing should fail, same instance expected", newRegistry.getImportError());
		assertEquals("Registry status is 'Processed with error'", RegistryStatus.PROCESSED_WITH_ERROR, newRegistry.getRegistryStatus().getCode());

		newRegistry = registryService.readWithContainers(stub(newRegistry));
		assertNotNull("Registry not found", newRegistry);
		removeInstanceIdFromContainer(newRegistry);
		parameters.put(StartRegistryProcessingActionHandler.REGISTRY_ID, newRegistry.getId());
		processId = testProcessManager.createProcess("ProcessingDBRegistryProcess", parameters);
		assertTrue("Process can not created", processId > 0);
		process = testProcessManager.getProcessInstanceInfo(processId);
		assertNotNull("Process did not find", process);
		do {
			log.debug("Wait completed process {}", processId);
			Thread.sleep(100);
			process = testProcessManager.getProcessInstanceInfo(processId);
		} while(!process.getProcessState().isCompleted());

		newRegistry = registryService.readWithContainers(stub(newRegistry));
		assertNotNull("Registry not found", newRegistry);
		assertNull("Processing should not fail", newRegistry.getImportError());
		assertEquals("Registry should have success status",
				RegistryStatus.PROCESSED_WITH_ERROR, newRegistry.getRegistryStatus().getCode());

		Long afterProcessOperationsCount = (Long) DataAccessUtils.uniqueResult(hibernateTemplate.find(hql));

		assertEquals("Processing should add new operations", operationsCount, afterProcessOperationsCount);
	}

	@Transactional(readOnly = false)
	private void removeInstanceIdFromContainer(Registry registry) throws FlexPayException {
		for (RegistryContainer container : registry.getContainers()) {
			if (container.getData().startsWith("503:")) {
				container.setData("");
			}
		}
		registryService.update(registry);
	}
}
