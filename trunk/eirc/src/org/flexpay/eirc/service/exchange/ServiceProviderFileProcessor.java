package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.dao.importexport.InMemoryRawConsumersDataSource;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.exchange.InvalidContainerException;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
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

	/**
	 * Run processing of a provider data file
	 *
	 * @param file uploaded SpFile
	 */
	public void processFile(SpFile file) {

		if (log.isInfoEnabled()) {
			log.info("Starting processing file");
		}

		List<SpRegistry> registries = spFileService.getRegistries(file);
		if (log.isInfoEnabled() && registries.isEmpty()) {
			log.info("File does not have any registries");
		}

		processRegistries(registries);
	}

	public void processRegistries(Collection<SpRegistry> registries) {

		for (SpRegistry registry : registries) {
			try {
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
				List<SpRegistryRecord> records = spFileService.getRegistryRecords(registry);
				for (SpRegistryRecord record : records) {
					processRecord(registry, record);
				}
			} catch (Exception e) {
				log.error("Failed processing registry: " + registry, e);
			}
		}
	}

	public void processRecords(SpRegistry registry, Collection<SpRegistryRecord> records) {
		RawDataSource<RawConsumerData> dataSource = new InMemoryRawConsumersDataSource(records);
		setupRecordsConsumer(registry, dataSource);

		for (SpRegistryRecord record : records) {
			processRecord(registry, record);
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
	 */
	private void processRecord(SpRegistry registry, SpRegistryRecord record) {
		try {
			Operation op = serviceOperationsFactory.getOperation(registry, record);
			op.process(registry, record);
		} catch (FlexPayException e) {
			log.error("Failed processing registry record containers: " + registry, e);
		}
	}

	/**
	 * Setter for property 'operationFactory'.
	 *
	 * @param serviceOperationsFactory Value to set for property 'operationFactory'.
	 */
	public void setServiceOperationsFactory(ServiceOperationsFactory serviceOperationsFactory) {
		this.serviceOperationsFactory = serviceOperationsFactory;
	}

	/**
	 * Setter for property 'spFileService'.
	 *
	 * @param spFileService Value to set for property 'spFileService'.
	 */
	public void setSpFileService(SpFileService spFileService) {
		this.spFileService = spFileService;
	}

	/**
	 * Setter for property 'importService'.
	 *
	 * @param importService Value to set for property 'importService'.
	 */
	public void setImportService(EircImportService importService) {
		this.importService = importService;
	}

	/**
	 * Setter for property 'rawConsumersDataSource'.
	 *
	 * @param rawConsumersDataSource Value to set for property 'rawConsumersDataSource'.
	 */
	public void setRawConsumersDataSource(RawConsumersDataSource rawConsumersDataSource) {
		this.rawConsumersDataSource = rawConsumersDataSource;
	}
}
