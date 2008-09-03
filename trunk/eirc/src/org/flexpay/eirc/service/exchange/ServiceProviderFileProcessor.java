package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.InMemoryRawConsumersDataSource;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.workflow.RegistryWorkflowManager;
import org.flexpay.eirc.persistence.workflow.TransitionNotAllowed;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.SpRegistryService;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc.
 */
public class ServiceProviderFileProcessor implements RegistryProcessor {

	private Logger log = Logger.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private SpFileService spFileService;
	private SpRegistryService spRegistryService;
	private SpRegistryRecordService registryRecordService;

	private EircImportService importService;

	private RawConsumersDataSource rawConsumersDataSource;
	private ImportErrorsSupport errorsSupport;
	private RegistryWorkflowManager registryWorkflowManager;

	private ServiceProviderFileProcessorTx processorTx;

	/**
	 * Run processing of a registry data file
	 *
	 * @param file uploaded SpFile
	 * @throws Exception if failure occurs
	 */
	public void processFile(@NotNull SpFile file) throws Exception {

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
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public void processRegistries(@NotNull Collection<SpRegistry> registries) throws Exception {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		for (SpRegistry registry : registries) {

			try {
				startRegistryProcessing(registry);

				if (log.isInfoEnabled()) {
					log.info("Starting processing registry #" + registry.getId());
				}

				importConsumers(registry);
				processRegistry(registry);
			} catch (Throwable e) {
				String errMsg = "Failed processing registry: " + registry;
				log.error(errMsg, e);
				container.addException(new FlexPayException(errMsg, e));
			} finally {
				endRegistryProcessing(registry);
			}
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	public void processRegistry(SpRegistry registry) throws Exception {

		log.info("Starting processing records");
		Page<RegistryRecord> pager = new Page<RegistryRecord>(500, 1);
		Long[] minMaxIds = {null, null};
		do {
			processorTx.processRegistry(registry, pager, minMaxIds);
		} while (pager.getThisPageFirstElementNumber() <= minMaxIds[1]);
		log.info("No more records to process");
	}

	public void importConsumers(SpRegistry registry) throws Exception {

		serviceOperationsFactory.setDataSource(rawConsumersDataSource);
		processHeader(registry);
		log.info("Starting importing consumers");
		rawConsumersDataSource.setRegistry(registry);

		setupRecordsConsumer(registry, rawConsumersDataSource);
	}

	public void startRegistryProcessing(SpRegistry registry) throws TransitionNotAllowed {
		registryWorkflowManager.startProcessing(registry);
	}

	public void endRegistryProcessing(SpRegistry registry) throws TransitionNotAllowed {
		registry = spRegistryService.read(stub(registry));
		registryWorkflowManager.setNextSuccessStatus(registry);
		registryWorkflowManager.endProcessing(registry);
	}

	public void processRecords(SpRegistry registry, Set<Long> recordIds) throws Exception {

		try {
			startRegistryProcessing(registry);
			setupRecordsConsumers(registry, recordIds);

			// refresh records
			Collection<RegistryRecord> records = registryRecordService.findObjects(registry, recordIds);
			for (RegistryRecord record : records) {
				processorTx.processRecord(registry, record);
			}
		} catch (Throwable t) {
			String errMsg = "Failed processing registry: " + registry;
			log.error(errMsg, t);
			throw new FlexPayException(errMsg, t);
		} finally {
			endRegistryProcessing(registry);
		}
	}

	private void setupRecordsConsumers(SpRegistry registry, Set<Long> recordIds) throws Exception {

		Collection<RegistryRecord> records = registryRecordService.findObjects(registry, recordIds);
		RawDataSource<RawConsumerData> dataSource = new InMemoryRawConsumersDataSource(records);
		errorsSupport.registerAlias(dataSource, rawConsumersDataSource);
		serviceOperationsFactory.setDataSource(dataSource);

		try {
			// setup records registry to the same object
			for (RegistryRecord record : records) {
				if (stub(record.getSpRegistry()).equals(stub(registry))) {
					record.setSpRegistry(registry);
				} else {
					throw new FlexPayException("Only records of the same registry are allowed for group processing");
				}
			}

			setupRecordsConsumer(registry, dataSource);
		} finally {
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
	 * @throws Exception if failure occurs
	 */
	private void setupRecordsConsumer(SpRegistry registry, RawDataSource<RawConsumerData> rawConsumersDataSource)
			throws Exception {

		importService.importConsumers(
				registry.getServiceProvider().getDataSourceDescription(),
				rawConsumersDataSource);
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

	public void setRegistryRecordService(SpRegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	public void setProcessorTx(ServiceProviderFileProcessorTx processorTx) {
		this.processorTx = processorTx;
	}
}
