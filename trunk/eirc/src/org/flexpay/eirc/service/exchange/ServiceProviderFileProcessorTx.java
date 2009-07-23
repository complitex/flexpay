package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br />
 * Precondition for processing file is complete import operation, i.e. all records should already have assigned
 * PersonalAccount.
 */
@Transactional (readOnly = true)
public interface ServiceProviderFileProcessorTx {

	/**
	 * Run processing on single registry record
	 *
	 * @param registry Registry header
	 * @param record   Registry record
	 * @throws Exception if failure occurs
	 */
	void processRecord(Registry registry, RegistryRecord record) throws Exception;
}
