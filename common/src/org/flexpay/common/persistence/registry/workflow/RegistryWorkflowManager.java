package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * Helper class for registries workflow
 */
@Transactional (readOnly = true)
public interface RegistryWorkflowManager {


	/**
	 * Check if registry status transition allowed
	 *
	 * @param registry   Registry to check
	 * @param nextStatus Registry status to set up
	 * @return <code>true</code> if registry processing allowed, or <code>false</code> otherwise
	 */
	boolean canTransit(Registry registry, RegistryStatus nextStatus);

	/**
	 * Check if registry can be processed, i.e. has one of the following statuses: {@link
	 * org.flexpay.common.persistence.registry.RegistryStatus#LOADED}, {@link org.flexpay.common.persistence.registry.RegistryStatus#ROLLBACKED},
	 * {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_CANCELED} or {@link
	 * org.flexpay.common.persistence.registry.RegistryStatus#PROCESSED_WITH_ERROR}
	 *
	 * @param registry Registry to check
	 * @return <code>true</code> if registry is allowed to be processed
	 */
	boolean canProcess(Registry registry);

	void startProcessing(Registry registry) throws TransitionNotAllowed;

	void endProcessing(Registry registry) throws TransitionNotAllowed;

	/**
	 * Set next error registry status
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if error transition is not allowed
	 */
	void setNextErrorStatus(Registry registry) throws TransitionNotAllowed;


	/**
	 * Set next error registry record status and setup error
	 *
	 * @param registry Registry to update
	 * @param error  ImportError
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if error transition is not allowed
	 */
	void setNextErrorStatus(Registry registry, ImportError error) throws TransitionNotAllowed;

	/**
	 * Set next success registry status
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if success transition is not allowed
	 */
	void setNextSuccessStatus(Registry registry) throws TransitionNotAllowed;

	/**
	 * Set initial registry status
	 *
	 * @param registry Registry to update
	 * @return SpRegistry back
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if registry already has a status
	 */
	Registry setInitialStatus(Registry registry) throws TransitionNotAllowed;

	/**
	 * Set next registry status
	 *
	 * @param registry Registry to update
	 * @param status   Next status to set
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if transition from old to a new status is not allowed
	 */
	void setNextStatus(Registry registry, RegistryStatus status) throws TransitionNotAllowed;

	/**
	 * Set registry processing status to {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_WITH_ERROR}
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if registry status is not {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING} or
	 *          {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_WITH_ERROR}
	 */
	void markProcessingHasError(Registry registry) throws TransitionNotAllowed;
}
