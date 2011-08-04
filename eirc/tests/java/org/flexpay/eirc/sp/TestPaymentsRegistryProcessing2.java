package org.flexpay.eirc.sp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.drools.utils.WorkItemCompleteLocker;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryContainer;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.process.ProcessDefinitionManager;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.eirc.process.registry.helper.ProcessingRegistryConstants;
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

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.DateUtil.parseDate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

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
	@Qualifier ("registryService")
	private RegistryService registryService;
	@Autowired
	private ProcessManager processManager;
	@Autowired
	protected ProcessDefinitionManager processDefinitionManager;

	@Test
	public void testGeneratePaymentsRegistryAndProcess() throws Exception {
		processDefinitionManager.deployProcessDefinition("ProcessingDBRegistry", true);

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
		Long operationsCount = (Long) DataAccessUtils.uniqueResult(jpaTemplate.find(hql));

		Registry newRegistry = registries.get(0);
		log.debug("Uploaded registry: {}", newRegistry);
		Map<String, Object> parameters = map();
		parameters.put(ProcessingRegistryConstants.REGISTRY_ID, newRegistry.getId());
		ProcessInstance process = processManager.startProcess("ProcessingDBRegistry", parameters);
		assertNotNull("ProcessInstance can not created", process);
		waitWhileProcessInstanceWillComplete(process);

		EntityManager entityManager = jpaTemplate.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		Registry processedRegistry = entityManager.find(Registry.class, newRegistry.getId());
		log.debug("Processed registry: {}", processedRegistry);

		log.debug("Processed registry ImportError: {}", processedRegistry.getImportError());
		assertNotNull("Processing should fail, same instance expected", processedRegistry.getImportError());
		assertEquals("Registry status is 'Processed with error'", RegistryStatus.PROCESSED_WITH_ERROR, processedRegistry.getRegistryStatus().getCode());

		assertNotNull("Registry not found", processedRegistry);
		removeInstanceIdFromContainer(processedRegistry);
		entityManager.merge(processedRegistry);
		entityManager.getTransaction().commit();
		parameters = map();
		parameters.put(ProcessingRegistryConstants.REGISTRY_ID, newRegistry.getId());
		process = processManager.startProcess("ProcessingDBRegistry", parameters);
		assertNotNull("ProcessInstance can not created", process);
		waitWhileProcessInstanceWillComplete(process);

		entityManager = jpaTemplate.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		processedRegistry = entityManager.find(Registry.class, newRegistry.getId());
		assertNotNull("Registry not found", processedRegistry);
		assertNull("Processing should not fail", processedRegistry.getImportError());
		assertEquals("Registry should have success status",
				RegistryStatus.PROCESSED_WITH_ERROR, processedRegistry.getRegistryStatus().getCode());
		entityManager.close();

		Long afterProcessOperationsCount = (Long)DataAccessUtils.uniqueResult(jpaTemplate.find(hql));

		assertEquals("Processing should add new operations", operationsCount, afterProcessOperationsCount);
	}

	private void removeInstanceIdFromContainer(Registry registry) throws FlexPayException {
		for (RegistryContainer container : registry.getContainers()) {
			if (container.getData().startsWith("503:")) {
				container.setData("503:001");
			}
		}
	}

	private ProcessInstance waitWhileProcessInstanceWillComplete(ProcessInstance processInstance) throws InterruptedException {
		ProcessInstance pi = null;
		while(true) {
			WorkItemCompleteLocker.lock();
			log.debug("Get process instance: {}", processInstance.getId());
			try {
				pi = processManager.getProcessInstance(processInstance.getId());
			} catch (RuntimeException e) {
				log.error("RuntimeException", e);
				throw e;
			} finally {
				WorkItemCompleteLocker.unlock();
			}
			log.debug("Got process instance: {}", pi);
			if (pi.hasEnded()) {
				log.debug("End process instance");
				break;
			}
			Thread.sleep(500);
		}
		return pi;
	}
}
