package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.InMemoryRawConsumersDataSource;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.RegistryWorkflowManager;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Processor of instructions specified by service provider, usually payments, balance
 * notifications, etc. <br /> Precondition for processing file is complete import
 * operation, i.e. all records should already have assigned PersonalAccount.
 */
public class ServiceProviderFileProcessor {

	private Logger log = Logger.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private SpFileService spFileService;
	private SpRegistryService spRegistryService;
	private SpRegistryRecordService registryRecordService;

	private EircImportService importService;

	private RawConsumersDataSource rawConsumersDataSource;
	private ImportErrorsSupport errorsSupport;
	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	/**
	 * Run processing of a registry data file
	 *
	 * @param file uploaded SpFile
	 * @throws FlexPayExceptionContainer if registry processing failed
	 * @throws Exception				 if failure occurs
	 */
	public void processFile(SpFile file) throws Exception {

		if (log.isInfoEnabled()) {
			log.info("Starting processing file");
		}

		List<SpRegistry> registries = spFileService.getRegistries(file);
		if (log.isInfoEnabled() && registries.isEmpty()) {
			log.info("File does not have any registries");
		}

		processRegistries(registries);
	}

	/**
	 * Run processing of a <code>registries</code>
	 *
	 * @param registries Registries to process
	 * @throws FlexPayExceptionContainer if registry processing failed
	 * @throws Exception				 if failure occurs
	 */
	public void processRegistries(Collection<SpRegistry> registries) throws Exception {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		for (SpRegistry registry : registries) {

			try {
				registryWorkflowManager.startProcessing(registry);

				if (log.isInfoEnabled()) {
					log.info("Starting processing registry #" + registry.getId());
				}

				serviceOperationsFactory.setDataSource(rawConsumersDataSource);
				processHeader(registry);

				log.info("Starting importing consumers");
				rawConsumersDataSource.setRegistry(registry);
				setupRecordsConsumer(registry, rawConsumersDataSource);

				log.info("Starting processing records");
				Page<SpRegistryRecord> pager = new Page<SpRegistryRecord>(50, 1);
				List<SpRegistryRecord> records;
				do {
					records = spFileService.getRecordsForProcessing(registry, pager);
					for (SpRegistryRecord record : records) {
						processRecord(registry, record);
					}
					pager.setPageNumber(pager.getPageNumber() + 1);
					spFileService.clearSession();
				} while (!records.isEmpty());
			} catch (Exception e) {
				String errMsg = "Failed processing registry: " + registry;
				log.error(errMsg, e);
				container.addException(new FlexPayException(errMsg, e));
			} finally {
				registry = spRegistryService.read(registry.getId());
				registryWorkflowManager.setNextSuccessStatus(registry);
				registryWorkflowManager.endProcessing(registry);
			}
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	public void processRecords(SpRegistry registry, Set<Long> objectIds) throws Exception {
		Collection<SpRegistryRecord> records = registryRecordService.findObjects(registry, objectIds);
		RawDataSource<RawConsumerData> dataSource = new InMemoryRawConsumersDataSource(records);
		errorsSupport.registerAlias(dataSource, rawConsumersDataSource);
		serviceOperationsFactory.setDataSource(dataSource);

		try {
			// setup records registry to the same object
			for (SpRegistryRecord record : records) {
				if (record.getSpRegistry().getId().equals(registry.getId())) {
					record.setSpRegistry(registry);
				} else {
					throw new FlexPayException("Only records of the same registry allowed for group processing");
				}
			}

			registryWorkflowManager.startProcessing(registry);
			setupRecordsConsumer(registry, dataSource);

			// refresh records
			records = registryRecordService.findObjects(registry, objectIds);
			for (SpRegistryRecord record : records) {
				processRecord(registry, record);
			}
		} catch (Exception e) {
			String errMsg = "Failed processing registry: " + registry;
			log.error(errMsg, e);
			throw new FlexPayException(errMsg, e);
		} finally {
			registry = spRegistryService.read(registry.getId());
			registryWorkflowManager.setNextSuccessStatus(registry);
			registryWorkflowManager.endProcessing(registry);
			errorsSupport.unregisterAlias(dataSource);
		}
	}

	/**
	 * Run processing on registry header
	 *
	 * @param registry Registry header
	 * @throws Exception if failure occurs
	 */
	private void processHeader(SpRegistry registry) throws Exception {

		// process header containers
		try {
			Operation op = serviceOperationsFactory.getContainerOperation(registry);
			op.process(registry, null);
		} catch (Exception e) {
			log.error("Failed constructing container for registry: " + registry, e);
			// in processing, notify have error
			registryWorkflowManager.setNextErrorStatus(registry);
			throw e;
		}
	}

	/**
	 * Set up consumers for records
	 *
	 * @param registry			   SpRegistry to process
	 * @param rawConsumersDataSource Consumers data source
	 * @return <code>true</code> if setup run without errors, or <code>false</code>
	 *         otherwise
	 */
	private boolean setupRecordsConsumer(SpRegistry registry, RawDataSource<RawConsumerData> rawConsumersDataSource) {
		try {
			importService.importConsumers(
					registry.getServiceProvider().getDataSourceDescription(),
					rawConsumersDataSource);
			return true;
		} catch (FlexPayException e) {
			log.error("Failed setting consumers", e);
			return false;
		}
	}

	/**
	 * Run processing on single registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws Exception if failure occurs
	 */
	private void processRecord(SpRegistry registry, SpRegistryRecord record) throws Exception {
		try {
			if (!recordWorkflowManager.hasSuccessTransition(record)) {
				if (log.isDebugEnabled()) {
					log.debug("Skipping record: " + record);
				}
				return;
			}
			Operation op = serviceOperationsFactory.getOperation(registry, record);
			op.process(registry, record);
			recordWorkflowManager.setNextSuccessStatus(record);
		} catch (Exception e) {
			log.error("Failed processing registry record: " + record, e);
			recordWorkflowManager.setNextErrorStatus(record);
		}
	}

	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	public void setSpRegistryService(SpRegistryService spRegistryService) {
		this.spRegistryService = spRegistryService;
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public void setImportService(EircImportService importService) {
		this.importService = importService;
	}

	public void setRawConsumersDataSource(RawConsumersDataSource rawConsumersDataSource) {
		this.rawConsumersDataSource = rawConsumersDataSource;
	}

	public void setErrorsSupport(ImportErrorsSupport errorsSupport) {
		this.errorsSupport = errorsSupport;
	}

	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	public void setRegistryRecordService(SpRegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}
}
