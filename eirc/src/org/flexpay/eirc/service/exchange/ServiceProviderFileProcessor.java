package org.flexpay.eirc.service.exchange;

import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.exchange.ServiceOperationsFactory;
import org.flexpay.eirc.persistence.exchange.Operation;
import org.flexpay.eirc.persistence.exchange.InvalidContainerException;
import org.flexpay.eirc.service.SpFileService;
import org.flexpay.eirc.service.SPService;
import org.flexpay.common.exception.FlexPayException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc.
 * <br />
 * Precondition for processing file is complete import operation, i.e. all records
 * should already have assigned PersonalAccount.
 */
public class ServiceProviderFileProcessor {

	private ServiceOperationsFactory serviceOperationsFactory;

	private SpFileService spFileService;
	private SPService spService;

	private static Logger log = Logger.getLogger(ServiceProviderFileProcessor.class);

	public void processFile(SpFile file) {
		List<SpRegistry> registries = spFileService.getRegistries(file);
		for (SpRegistry registry : registries) {
			processHeader(registry);

			List<SpRegistryRecord> records = spFileService.getRegistryRecords(registry);
			for (SpRegistryRecord record : records) {
				processRecords(registry, record);
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

		try {
			Operation op = serviceOperationsFactory.getContainer(registry);
			op.process(registry, null);
		} catch (InvalidContainerException e) {
			log.error("Failed constructing container for registry: " + registry, e);
		} catch (FlexPayException e) {
			log.error("Failed processing registry header containers: " + registry, e);
		}
	}

	/**
	 * Run processing on single registry record
	 *
	 * @param registry Registry header
	 * @param record Registry record
	 */
	private void processRecords(SpRegistry registry, SpRegistryRecord record) {

		ServiceType type = spService.getServiceType(record.getServiceCode().intValue());
		Service service = spService.getService(registry.getServiceProvider(), type);
		record.setService(service);

		try {
			Operation op = serviceOperationsFactory.getContainer(record);
			op.process(registry, record);
		} catch (InvalidContainerException e) {
			log.error("Failed constructing container for registry record: " + record, e);
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
}
