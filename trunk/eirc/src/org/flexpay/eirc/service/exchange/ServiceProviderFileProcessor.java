package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHolder;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.ImportErrorsSupport;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.InMemoryRawConsumersDataSource;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.exchange.DelayedUpdate;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc.
 */
public class ServiceProviderFileProcessor implements RegistryProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private RegistryFileService registryFileService;
	private RegistryService registryService;
	private RegistryRecordService registryRecordService;

	private EircImportService importService;

	private RawConsumersDataSource rawConsumersDataSource;
	private ImportErrorsSupport errorsSupport;
	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private ClassToTypeRegistry classToTypeRegistry;

	private ServiceProviderService serviceProviderService;
	private ServiceProviderFileProcessorTx processorTx;

	/**
	 * Run processing of a registry data file
	 *
	 * @param spFile uploaded spFile
	 * @throws Exception if failure occurs
	 */
	public void processFile(@NotNull FPFile spFile) throws Exception {

		log.info("Starting processing file");

		List<Registry> registries = registryFileService.getRegistries(spFile);
		if (registries.isEmpty()) {
			log.info("File does not have any registries");
		}

		registriesProcess(registries);
	}

	/**
	 * Run processing of a <code>registries</code>
	 *
	 * @param registries Registries to process
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *                   if registry processing failed
	 * @throws Exception if failure occurs
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	public void registriesProcess(@NotNull Collection<Registry> registries) throws Exception {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		for (Registry registry : registries) {

			try {
				startRegistryProcessing(registry);

				log.info("Starting processing registry #{}", registry.getId());

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

	public void processRegistry(@NotNull Registry registry) throws Exception {

		StopWatch watch = new StopWatch();
		watch.start();
		Logger plog = ProcessLogger.getLogger(getClass());

		plog.info("Starting processing records");
		FetchRange range = new FetchRange(50);
		ReadHintsHolder.setHints(new ReadHints());
		do {
			plog.info("Fetching next page {}. Time spent {}", range, watch);
			processRegistry(registry, range);
		} while (range.hasMore());
		plog.info("No more records to process");

		watch.stop();
		plog.info("Import finished. Time spent {}", watch);
	}

	/**
	 * Run next registry records batch processing
	 *
	 * @param registry Registry those records to process
	 * @param range	Fetch range
	 * @throws Exception if failure occurs
	 */
	private void processRegistry(Registry registry, FetchRange range) throws Exception {

		List<RegistryRecord> records = registryFileService.getRecordsForProcessing(stub(registry), range);
		for (RegistryRecord record : records) {
			try {
				processorTx.processRecord(registry, record);
			} catch (Throwable t) {
				handleError(t, registry, record);
			}
		}
		range.nextPage();
	}

	private void handleError(Throwable t, Registry registry, RegistryRecord record) throws Exception {
		String code = "eirc.error_code.unknown_error";
		if (t instanceof FlexPayExceptionContainer) {
			t = ((FlexPayExceptionContainer) t).getExceptions().iterator().next();
			code = "eirc.error_code.error_with_processing_container";
		}

		log.warn("Failed processing registry record", t);

		ImportError error = new ImportError();
		error.setErrorId(code);
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = serviceProviderService.read(props.getServiceProviderStub());
		DataSourceDescription sd = provider.getDataSourceDescription();
		error.setSourceDescription(sd);

		// todo remove hardcoded value
		error.setDataSourceBean("consumersDataSource");

		error.setSourceObjectId(String.valueOf(record.getId()));
		error.setObjectType(classToTypeRegistry.getType(Consumer.class));
		recordWorkflowManager.setNextErrorStatus(record, error);
	}

	public void importConsumers(Registry registry) throws Exception {

		processHeader(registry);
		log.info("Starting importing consumers");
		rawConsumersDataSource.setRegistry(registry);

		setupRecordsConsumer(registry, rawConsumersDataSource);
	}

	public void startRegistryProcessing(Registry registry) throws TransitionNotAllowed {
		registryWorkflowManager.startProcessing(registry);
	}

	public void endRegistryProcessing(Registry registry) throws TransitionNotAllowed {
		registry = registryService.read(stub(registry));
		registryWorkflowManager.setNextSuccessStatus(registry);
		registryWorkflowManager.endProcessing(registry);
	}

	public void processRecords(Registry registry, Set<Long> recordIds) throws Exception {

		try {
			startRegistryProcessing(registry);
			setupRecordsConsumers(registry, recordIds);

			// refresh records
			Collection<RegistryRecord> records = registryRecordService.findObjects(registry, recordIds);
			for (RegistryRecord record : records) {
				try {
					processorTx.processRecord(registry, record);
				} catch (Throwable t) {
					handleError(t, registry, record);
				}
			}
		} catch (Throwable t) {
			String errMsg = "Failed processing registry: " + registry;
			log.error(errMsg, t);
			throw new FlexPayException(errMsg, t);
		} finally {
			endRegistryProcessing(registry);
		}
	}

	private void setupRecordsConsumers(Registry registry, Set<Long> recordIds) throws Exception {

		Collection<RegistryRecord> records = registryRecordService.findObjects(registry, recordIds);
		RawDataSource<RawConsumerData> dataSource = new InMemoryRawConsumersDataSource(records);
		errorsSupport.registerAlias(dataSource, rawConsumersDataSource);

		try {
			// setup records registry to the same object
			for (RegistryRecord record : records) {
				if (stub(record.getRegistry()).equals(stub(registry))) {
					record.setRegistry(registry);
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
	private void processHeader(Registry registry) throws Exception {

		// process header containers
		try {
			Operation op = serviceOperationsFactory.getContainerOperation(registry);
			DelayedUpdate update = op.process(registry, null);
			update.doUpdate();
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
	private void setupRecordsConsumer(Registry registry, RawDataSource<RawConsumerData> rawConsumersDataSource)
			throws Exception {

		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		ServiceProvider provider = serviceProviderService.read(props.getServiceProviderStub());
		Stub<DataSourceDescription> sd = provider.getDataSourceDescriptionStub();
		importService.importConsumers(sd, rawConsumersDataSource);
	}

	@Required
	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryFileService(RegistryFileService registryFileService) {
		this.registryFileService = registryFileService;
	}

	@Required
	public void setImportService(EircImportService importService) {
		this.importService = importService;
	}

	@Required
	public void setRawConsumersDataSource(RawConsumersDataSource rawConsumersDataSource) {
		this.rawConsumersDataSource = rawConsumersDataSource;
	}

	@Required
	public void setErrorsSupport(ImportErrorsSupport errorsSupport) {
		this.errorsSupport = errorsSupport;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setProcessorTx(ServiceProviderFileProcessorTx processorTx) {
		this.processorTx = processorTx;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}

}
