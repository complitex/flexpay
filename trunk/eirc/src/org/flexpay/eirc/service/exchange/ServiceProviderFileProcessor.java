package org.flexpay.eirc.service.exchange;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
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
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.service.importexport.EircImportService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc.
 */
@Transactional(readOnly = true)
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

	private OrganizationService organizationService;
	private ServiceProviderFileProcessorTx processorTx;

	/**
	 * Run processing of a registry data file
	 *
	 * @param spFile uploaded spFile
	 * @throws Exception if failure occurs
	 */
	@Deprecated
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
	@Deprecated
	public void registriesProcess(@NotNull Collection<Registry> registries) throws Exception {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		for (Registry registry : registries) {

			ProcessingContext context = new ProcessingContext();
			context.setRegistry(registry);
			try {
				startRegistryProcessing(context);

				log.info("Starting processing registry #{}", registry.getId());

				importConsumers(context);
				processRegistry(context);
			} catch (Throwable e) {
				String errMsg = "Failed processing registry: " + registry;
				log.error(errMsg, e);
				container.addException(new FlexPayException(errMsg, e));
			} finally {
				endRegistryProcessing(context);
			}
		}

		if (!container.isEmpty()) {
			throw container;
		}
	}

	@Deprecated
	public void processRegistry(@NotNull ProcessingContext context) throws Exception {

		StopWatch watch = new StopWatch();
		watch.start();
		Logger plog = ProcessLogger.getLogger(getClass());

		try {
			plog.debug("Starting processing registry header");
			startRegistryProcessing(context);
			try {
				processorTx.processHeader(context);
			} catch (Throwable t) {
				handleError(t, context);
				return;
			}

			plog.info("Starting processing records");
			FetchRange range = new FetchRange(50);
			ReadHintsHolder.setHints(new ReadHints());
			Long operationId = null;
			do {
				plog.info("Fetching next page {}. Time spent {}", range, watch);
				List<RegistryRecord> records = registryFileService.getRecordsForProcessing(
						stub(context.getRegistry()), range);
				for (RegistryRecord record : records) {
					try {

						if (operationId == null || operationId.equals(record.getUniqueOperationNumber())) {
							operationId = record.getUniqueOperationNumber();
							processorTx.doUpdate(context);
						}

						context.setCurrentRecord(record);
						processorTx.prepareRecordUpdates(context);
					} catch (Throwable t) {
						handleError(t, context);
					}
				}
				range.nextPage();
			} while (range.hasMore());
			processorTx.doUpdate(context);
			plog.info("No more records to process");
		} finally {
			endRegistryProcessing(context);
		}

		watch.stop();
		plog.info("Processing finished. Time spent {}", watch);
	}

	@SuppressWarnings ({"ThrowableResultOfMethodCallIgnored"})
	@Deprecated
	private void handleError(Throwable t, ProcessingContext context) throws Exception {
		String code = "eirc.error_code.unknown_error";
		if (t instanceof FlexPayExceptionContainer) {
			t = ((FlexPayExceptionContainer) t).getFirstException();
		}
		if (t instanceof FlexPayException) {
			FlexPayException ex = (FlexPayException) t;
			code = StringUtils.isNotEmpty(ex.getErrorKey()) ?
				   ex.getErrorKey() : "eirc.error_code.error_with_processing_container";
		}

		log.warn("Failed processing registry", t);

		ImportError error = new ImportError();
		error.setErrorId(code);
		EircRegistryProperties props = (EircRegistryProperties) context.getRegistry().getProperties();
		Organization sender = organizationService.readFull(props.getSenderStub());
		if (sender == null) {
			throw new IllegalStateException("Cannot find sender organization: #" + props.getSenderStub().getId());
		}
		DataSourceDescription sd = sender.getDataSourceDescription();
		error.setSourceDescription(sd);

		error.setDataSourceBean("consumersDataSource");

		if (context.getCurrentRecord() == null) {
			error.setSourceObjectId(String.valueOf(context.getRegistry().getId()));
		} else {
			error.setSourceObjectId(String.valueOf(context.getCurrentRecord().getId()));
		}
		error.setObjectType(classToTypeRegistry.getType(Consumer.class));

		// also set error for operation records update
		for (RegistryRecord record : context.getOperationRecords()) {
			if (record != context.getCurrentRecord()) {
				recordWorkflowManager.setNextErrorStatus(record, error);
			}
		}

		// mark registry having error if processing header
		if (context.getOperationRecords().isEmpty()) {
			registryWorkflowManager.setNextErrorStatus(context.getRegistry(), error);
		}
	}

	@Deprecated
	public void importConsumers(ProcessingContext context) throws Exception {

		processHeader(context);
		log.info("Starting importing consumers");
		rawConsumersDataSource.setRegistry(context.getRegistry());

		setupRecordsConsumer(context.getRegistry(), rawConsumersDataSource);
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void startRegistryProcessing(ProcessingContext context) throws TransitionNotAllowed {
		if (context.isProcessingStarted()) {
			return;
		}
		registryWorkflowManager.startProcessing(context.getRegistry());
		context.startProcessing();
	}

	/**
	 * Run processing on registry header
	 *
	 * @param context ProcessingContext
	 * @throws Exception if failure occurs
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void processHeader(ProcessingContext context) throws Exception {

		// process header containers
		try {
			Operation op = serviceOperationsFactory.getContainerOperation(context.getRegistry());
			DelayedUpdate update = op.process(context);
			update.doUpdate();
		} catch (Exception e) {
			log.error("Failed constructing container for registry: " + context.getRegistry(), e);
			throw e;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void endRegistryProcessing(ProcessingContext context) throws TransitionNotAllowed {
		if (!context.isProcessingStarted() || context.isProcessingEnded()) {
			return;
		}
		Registry registry = registryService.read(stub(context.getRegistry()));
		registryWorkflowManager.setNextSuccessStatus(registry);
		registryWorkflowManager.endProcessing(registry);
		context.endProcessing();
	}

	@Deprecated
	public void processRecords(Registry registry, Set<Long> recordIds) throws Exception {

		ProcessingContext context = new ProcessingContext();
		context.setRegistry(registry);
		try {
			startRegistryProcessing(context);
			setupRecordsConsumers(registry, recordIds);

			// refresh records
			Collection<RegistryRecord> records = registryRecordService.findObjects(registry, recordIds);
			Long operationId = null;
			for (RegistryRecord record : records) {
				try {

					if (operationId == null || operationId.equals(record.getUniqueOperationNumber())) {
						operationId = record.getUniqueOperationNumber();
						processorTx.doUpdate(context);
					}

					context.setCurrentRecord(record);
					processorTx.prepareRecordUpdates(context);
				} catch (Throwable t) {
					handleError(t, context);
				}
			}
			processorTx.doUpdate(context);
		} catch (Throwable t) {
			String errMsg = "Failed processing registry: " + registry;
			log.error(errMsg, t);
			throw new FlexPayException(errMsg, t);
		} finally {
			endRegistryProcessing(context);
		}
	}

	@Deprecated
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
	 * Set up consumers for records
	 *
	 * @param registry			   SpRegistry to process
	 * @param rawConsumersDataSource Consumers data source
	 * @throws Exception if failure occurs
	 */
	@Deprecated
	private void setupRecordsConsumer(Registry registry, RawDataSource<RawConsumerData> rawConsumersDataSource)
			throws Exception {

		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Organization sender = organizationService.readFull(props.getSenderStub());
		if (sender == null) {
			throw new IllegalStateException("Cannot find sender organization: #" + props.getSenderStub().getId());
		}
		importService.importConsumers(sender.sourceDescriptionStub(), rawConsumersDataSource);
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
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
