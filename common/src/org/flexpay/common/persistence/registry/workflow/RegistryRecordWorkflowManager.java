package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Helper class for registry records workflow
 */
public interface RegistryRecordWorkflowManager {

	/**
	 * Check if registry record processing allowed
	 *
	 * @param record	 Registry to check
	 * @param nextStatus Registry status to set up
	 * @return <code>true</code> if registry processing allowed, or <code>false</code> otherwise
	 */
	boolean canTransit(RegistryRecord record, RegistryRecordStatus nextStatus);

	public boolean hasSuccessTransition(RegistryRecord record);

	/**
	 * Check if registry record is in state that allows processing, or moves it to an allowed one.
	 *
	 * @param record Registry record to start
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if record processing is not possible
	 */
	void startProcessing(RegistryRecord record) throws TransitionNotAllowed;

	/**
	 * Set next error registry record status
	 *
	 * @param record Registry record to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if error transition is not allowed
	 */
	void setNextErrorStatus(RegistryRecord record) throws TransitionNotAllowed;

	/**
	 * Set next error registry record status and setup error
	 *
	 * @param record Registry record to update
	 * @param error  ImportError
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if error transition is not allowed
	 */
	void setNextErrorStatus(RegistryRecord record, ImportError error) throws TransitionNotAllowed;

	/**
	 * Set next success registry record status
	 *
	 * @param record Registry record to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if success transition is not allowed
	 */
	void setNextSuccessStatus(RegistryRecord record) throws TransitionNotAllowed;

	/**
	 * Set next success registry records status
	 *
	 * @param records Registry records to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if success transition is not allowed for some of the records
	 */
	void setNextSuccessStatus(Collection<RegistryRecord> records) throws TransitionNotAllowed;

    void setNextStatusForErrorRecords(@NotNull Collection<RegistryRecord> records) throws TransitionNotAllowed;

	/**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	RegistryRecord removeError(RegistryRecord record);

	/**
	 * Set initial registry record status
	 *
	 * @param record Registry record to update
	 * @return SpRegistryRecord back
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if registry already has a status
	 */
	RegistryRecord setInitialStatus(RegistryRecord record) throws TransitionNotAllowed;

	/**
	 * Set next registry record status
	 *
	 * @param record Registry record to update
	 * @param status Next status to set
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if transition from old to a new status is not allowed
	 */
	void setNextStatus(RegistryRecord record, RegistryRecordStatus status) throws TransitionNotAllowed;
}
