package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.InMemoryRawConsumersDataSource;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.exchange.InvalidContainerException;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.persistence.workflow.RegistryWorkflowManager;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.service.importexport.RawConsumerData;

import java.util.Collection;
import java.util.List;

/**
 * Processor of instructions specified by service provider, usually payments, balance
 * notifications, etc. <br /> Precondition for processing file is complete import
 * operation, i.e. all records should already have assigned PersonalAccount.
 */
public class ServiceProviderFileProcessor {

	private Logger log = Logger.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private SpFileService spFileService;

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
				processHeader(registry);

				log.info("Starting importing consumers");
				rawConsumersDataSource.setRegistry(registry);
				if (!setupRecordsConsumer(registry, rawConsumersDataSource)) {
					continue;
				}

				log.info("Starting processing records");
				List<SpRegistryRecord> records = spFileService.getRecordsForProcessing(registry);
				for (SpRegistryRecord record : records) {
					processRecord(registry, record);
				}
				registryWorkflowManager.setNextSuccessStatus(registry);
			} catch (Exception e) {
				String errMsg = "Failed processing registry: " + registry;
				log.error(errMsg, e);
				registryWorkflowManager.setNextErrorStatus(registry);
				container.addException(new FlexPayException(errMsg, e));
			}
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}
	}

	public void processRecords(SpRegistry registry, Collection<SpRegistryRecord> records) throws Exception {
		RawDataSource<RawConsumerData> dataSource = new InMemoryRawConsumersDataSource(records);
		errorsSupport.registerAlias(dataSource, rawConsumersDataSource);

		try {
			registryWorkflowManager.startProcessing(registry);
			setupRecordsConsumer(registry, dataSource);

			for (SpRegistryRecord record : records) {
				processRecord(registry, record);
			}
			registryWorkflowManager.setNextSuccessStatus(registry);
		} catch (Exception e) {
			log.error("Failed processing registry records", e);
			registryWorkflowManager.setNextErrorStatus(registry);
		} finally {
			errorsSupport.unregisterAlias(dataSource);
		}
	}

	/**
	 * Run processing on registry header
	 *
	 * @param registry Registry header
	 */
	private void processHeader(SpRegistry registry) {

		// process header containers
		try {
			Operation op = serviceOperationsFactory.getContainerOperation(registry);
			op.process(registry, null);
		} catch (InvalidContainerException e) {
			log.error("Failed constructing container for registry: " + registry, e);
		} catch (FlexPayException e) {
			log.error("Failed processing registry header containers: " + registry, e);
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
			recordWorkflowManager.startProcessing(record);
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
}
