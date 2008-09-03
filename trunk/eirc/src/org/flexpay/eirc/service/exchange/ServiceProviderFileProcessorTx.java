package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.paging.Page;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.workflow.RegistryRecordWorkflowManager;
import org.flexpay.eirc.service.SpFileService;

import java.util.List;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br /> Precondition for
 * processing file is complete import operation, i.e. all records should already have assigned PersonalAccount.
 */
//@Transactional (readOnly = true)
public class ServiceProviderFileProcessorTx {

	private Logger log = Logger.getLogger(getClass());

	private ServiceOperationsFactory serviceOperationsFactory;

	private SpFileService spFileService;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private ClassToTypeRegistry classToTypeRegistry;

	/**
	 * Run next registry records batch processing
	 *
	 * @param registry  Registry those records to process
	 * @param pager	 Page
	 * @param minMaxIds Cached minimum and maximum registry record ids to process
	 * @throws Exception if failure occurs
	 */
//	@Transactional (readOnly = false)
	public void processRegistry(SpRegistry registry, Page<RegistryRecord> pager, Long[] minMaxIds) throws Exception {

		do {
			log.info("Fetching for records: " + pager);
			List<RegistryRecord> records = spFileService.getRecordsForProcessing(stub(registry), pager, minMaxIds);
			for (RegistryRecord record : records) {
				processRecord(registry, record);
			}
			pager.setPageNumber(pager.getPageNumber() + 1);
		} while (pager.getThisPageFirstElementNumber() <= (minMaxIds[1] - minMaxIds[0]));
		log.info("No more records to process");
	}

	/**
	 * Run processing on single registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws Exception if failure occurs
	 */
//	@Transactional (readOnly = false)
	public void processRecord(SpRegistry registry, RegistryRecord record) throws Exception {
		try {
			if (!recordWorkflowManager.hasSuccessTransition(record)) {
				if (log.isDebugEnabled()) {
					log.debug("Skipping record: " + record);
				}
				return;
			}

			if (log.isDebugEnabled()) {
				log.debug("Record to process: " + record);
			}

			Operation op = serviceOperationsFactory.getOperation(registry, record);
			op.process(registry, record);
			recordWorkflowManager.setNextSuccessStatus(record);
		} catch (Exception e) {
			log.warn("Failed processing registry record: " + e.getMessage());

			ImportError error = new ImportError();
			error.setErrorId(e.getMessage());
			error.setSourceDescription(registry.getServiceProvider().getDataSourceDescription());

			// todo remove hardcoded value
			error.setDataSourceBean("consumersDataSource");

			error.setSourceObjectId(String.valueOf(record.getId()));
			error.setObjectType(classToTypeRegistry.getType(Consumer.class));
			recordWorkflowManager.setNextErrorStatus(record, error);
		}
	}

	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}
}