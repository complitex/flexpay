package org.flexpay.eirc.service.exchange;

import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * Processor of instructions specified by service provider, usually payments, balance notifications, etc. <br />
 * Precondition for processing file is complete import operation, i.e. all records should already have assigned
 * PersonalAccount.
 */
@Transactional (readOnly = true)
public interface ServiceProviderFileProcessorTx {

	/**
	 * ProcessInstance header
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	void processHeader(@NotNull ProcessingContext context) throws Exception;

	/**
	 * Prepare delayed updates for single registry record
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	void prepareRecordUpdates(@NotNull ProcessingContext context) throws Exception;

	/**
	 * Do delayed update
	 *
	 * @param context ProcessingContext
	 * @throws Exception if failure occurs
	 */
	void doUpdate(@NotNull ProcessingContext context) throws Exception;
}
