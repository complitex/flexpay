package org.flexpay.eirc.service.exchange;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.InvalidContainerException;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.importexport.EircImportService;

import java.util.List;

/**
 * Processor of instructions specified by service provider, usually payments, balance
 * notifications, etc. <br /> Precondition for processing file is complete import
 * operation, i.e. all records should already have assigned PersonalAccount.
 */
public class ServiceProviderFileProcessor {

	private static Logger log = Logger.getLogger(ServiceProviderFileProcessor.class);

	private ServiceOperationsFactory serviceOperationsFactory;

	private SPService spService;
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

		for (SpRegistry registry : registries) {

			if (log.isInfoEnabled()) {
				log.info("Starting processing registry #" + registry.getId());			
			}
			processHeader(registry);

			if (log.isInfoEnabled()) {
				log.info("Starting importing consumers");
			}
			if (!setupRecordsConsumer(registry)) {
				continue;
			}

			if (log.isInfoEnabled()) {
				log.info("Starting processing records");
			}
			List<SpRegistryRecord> records = spFileService.getRegistryRecords(registry);
			for (SpRegistryRecord record : records) {
				processRecord(registry, record);
			}
		}
	}

	/**
	 * Run processing on registry header
	 *
	 * @param registry Registry header
	 */
	private void processHeader(SpRegistry registry) {
		ServiceProvider provider = spService.getProvider(registry.getSenderCode());
		if (provider == null) {
			log.error("Failed processing registry header, provider not found: #" + registry.getSenderCode());
			return;
		}

		registry.setServiceProvider(provider);

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
	 * @param registry SpRegistry to process
	 * @return <code>true</code> if setup run without errors, or <code>false</code>
	 *         otherwise
	 */
	private boolean setupRecordsConsumer(SpRegistry registry) {
		rawConsumersDataSource.setRegistry(registry);
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
	 * Setter for property 'spService'.
	 *
	 * @param spService Value to set for property 'spService'.
	 */
	public void setSpService(SPService spService) {
		this.spService = spService;
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
