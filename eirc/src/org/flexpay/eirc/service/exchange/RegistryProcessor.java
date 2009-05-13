package org.flexpay.eirc.service.exchange;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
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
	 * Handles {@link #startRegistryProcessing(org.flexpay.common.persistence.registry.Registry)} and {@link #endRegistryProcessing(org.flexpay.common.persistence.registry.Registry)} internally.
	 *
	 * @param registries Registries to process
	 * @throws Exception if failure occurs
	 */
	void registriesProcess(@NotNull Collection<Registry> registries) throws Exception;

	/**
	 * Process a limited number of registry records.
	 * <p/>
	 * Handles {@link #startRegistryProcessing(org.flexpay.common.persistence.registry.Registry)} and {@link #endRegistryProcessing(org.flexpay.common.persistence.registry.Registry)} internally.
	 *
	 * @param registry  Registry that records are to be processed
	 * @param recordIds Record identifiers
	 * @throws Exception if failure occurs
	 */
	void processRecords(Registry registry, Set<Long> recordIds) throws Exception;

	/**
	 * Start registry processing
	 *
	 * @param registry Registry to process
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void startRegistryProcessing(Registry registry) throws TransitionNotAllowed;

	/**
	 * Finish registry processing
	 *
	 * @param registry Registry to process
	 * @throws TransitionNotAllowed if processing is not allowed
	 */
	void endRegistryProcessing(Registry registry) throws TransitionNotAllowed;

	/**
	 * Setup consumers for registry records
	 *
	 * @param registry Registry to import
	 * @throws Exception if failure occurs
	 */
	void importConsumers(Registry registry) throws Exception;

	/**
	 * Process single registry, import operation should have bein completed before
	 *
	 * @param registry Registry to process
	 * @throws Exception if failure occurs
	 */
	void processRegistry(Registry registry) throws Exception;
}
