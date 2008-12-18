package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.workflow.TransitionNotAllowed;
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
	void processFile(@NotNull FlexPayFile spFile) throws Exception;

	/**
	 * Run processing of a <code>registries</code>
	 * <p/>
	 * Handles {@link #startRegistryProcessing(SpRegistry)} and {@link #endRegistryProcessing(SpRegistry)} internally.
	 *
	 * @param registries Registries to process
	 * @throws Exception if failure occurs
	 */
	void registriesProcess(@NotNull Collection<SpRegistry> registries) throws Exception;

	/**
	 * Process a limited number of registry records.
	 * <p/>
	 * Handles {@link #startRegistryProcessing(SpRegistry)} and {@link #endRegistryProcessing(SpRegistry)} internally.
	 *
	 * @param registry  Registry that records are to be processed
	 * @param recordIds Record identifiers
	 * @throws Exception if failure occurs
	 */
	void processRecords(SpRegistry registry, Set<Long> recordIds) throws Exception;

	/**
	 * Start registry processing
	 *
	 * @param registry Registry to process
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void startRegistryProcessing(SpRegistry registry) throws TransitionNotAllowed;

	/**
	 * Finish registry processing
	 *
	 * @param registry Registry to process
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void endRegistryProcessing(SpRegistry registry) throws TransitionNotAllowed;

	/**
	 * Setup consumers for registry records
	 *
	 * @param registry Registry to import
	 * @throws Exception if failure occurs
	 */
	void importConsumers(SpRegistry registry) throws Exception;

	/**
	 * Process single registry, import operation should have bein completed before
	 *
	 * @param registry Registry to process
	 * @throws Exception if failure occurs
	 */
	void processRegistry(SpRegistry registry) throws Exception;
}
