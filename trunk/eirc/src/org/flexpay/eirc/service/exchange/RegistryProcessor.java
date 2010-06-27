package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public interface RegistryProcessor {

	/**
	 * Run processing of a registry data file
	 *
	 * @param spFile uploaded spFile
	 * @throws Exception if failure occurs
	 */
	void processFile(@NotNull FPFile spFile) throws Exception;

	/**
	 * Run processing of a <code>registries</code>
	 * <p/>
	 * Handles {@link #startRegistryProcessing(org.flexpay.eirc.persistence.exchange.ProcessingContext)} and {@link
	 * #endRegistryProcessing(ProcessingContext)} internally.
	 *
	 * @param registries Registries to process
	 * @throws Exception if failure occurs
	 */
	void registriesProcess(@NotNull Collection<Registry> registries) throws Exception;

	/**
	 * Process a limited number of registry records.
	 * <p/>
	 * Handles {@link #startRegistryProcessing(org.flexpay.eirc.persistence.exchange.ProcessingContext)} and {@link
	 * #endRegistryProcessing(ProcessingContext)} internally.
	 *
	 * @param registry  Registry that records are to be processed
	 * @param recordIds Record identifiers
	 * @throws Exception if failure occurs
	 */
	void processRecords(Registry registry, Set<Long> recordIds) throws Exception;

	/**
	 * Start registry processing
	 *
	 * @param context ProcessingContext 
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void startRegistryProcessing(ProcessingContext context) throws TransitionNotAllowed;

	/**
	 * Finish registry processing
	 *
	 * @param context ProcessingContext 
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void endRegistryProcessing(ProcessingContext context) throws TransitionNotAllowed;

	/**
	 * Setup consumers for registry records
	 *
	 * @param context Processing context
	 * @throws Exception if failure occurs
	 */
	void importConsumers(ProcessingContext context) throws Exception;

	/**
	 * Process single registry, import operation should have bein completed before
	 *
	 * @param registry Registry to process
	 * @throws Exception if failure occurs
	 */
	void processRegistry(@NotNull ProcessingContext registry) throws Exception;

	/**
	 * Run processing on registry header
	 *
	 * @param context ProcessingContext
	 * @throws Exception if failure occurs
	 */
	public void processHeader(ProcessingContext context) throws Exception;
}
