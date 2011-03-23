package org.flexpay.common.persistence.registry.workflow;

import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.flexpay.common.service.RegistryRecordStatusService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.registry.RegistryRecordStatus.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;

/**
 * Helper class for registry records workflow
 */
@Transactional (readOnly = true)
public class RegistryRecordWorkflowManagerRO implements RegistryRecordWorkflowManager {

	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryWorkflowManager registryWorkflowManager;
	protected RegistryRecordStatusService statusService;

	protected Map<Integer, List<Integer>> transitions = map();

	{
		List<Integer> targets = list();
		targets.add(PROCESSED);
		targets.add(PROCESSED_WITH_ERROR);
		transitions.put(LOADED, targets);

		targets = list();
		targets.add(FIXED);
		transitions.put(PROCESSED_WITH_ERROR, targets);

		targets = list();
		targets.add(PROCESSED);
		targets.add(PROCESSED_WITH_ERROR);
		transitions.put(FIXED, targets);

		transitions.put(PROCESSED, Collections.<Integer>emptyList());
	}

	/**
	 * Check if registry record processing allowed
	 *
	 * @param record	 Registry to check
	 * @param nextStatus Registry status to set up
	 * @return <code>true</code> if registry processing allowed, or <code>false</code> otherwise
	 */
    @Override
	public boolean canTransit(RegistryRecord record, RegistryRecordStatus nextStatus) {
		return transitions.get(code(record)).contains(nextStatus.getCode());
	}

    @Override
	public boolean hasSuccessTransition(RegistryRecord record) {
		return !transitions.get(code(record)).isEmpty();
	}

	/**
	 * Check if registry record is in state that allows processing, or moves it to an allowed one.
	 *
	 * @param record Registry record to start
	 * @throws TransitionNotAllowed if record processing is not possible
	 */
    @Override
	public void startProcessing(RegistryRecord record) throws TransitionNotAllowed {
		if (!hasSuccessTransition(record)) {
			throw new TransitionNotAllowed("Registry processing not allowed");
		}

		if (code(record) == PROCESSED_WITH_ERROR) {
			setNextSuccessStatus(record);
		}
	}

	/**
	 * Helper status code extractor
	 *
	 * @param record Registry Record
	 * @return record status
	 */
	protected Integer code(RegistryRecord record) {
		return record.getRecordStatus().getCode();
	}

	/**
	 * Set next error registry record status
	 *
	 * @param record Registry record to update
	 * @throws TransitionNotAllowed if error transition is not allowed
	 */
    @Override
	public void setNextErrorStatus(RegistryRecord record) throws TransitionNotAllowed {

		if (code(record) == PROCESSED_WITH_ERROR) {
			return;
		}

		List<Integer> allowedCodes = transitions.get(code(record));
		if (allowedCodes.size() < 2) {
			throw new TransitionNotAllowed("No error transition, current is: " + code(record));
		}

		markRegistryAsHavingError(record);
		setNextStatus(record, allowedCodes.get(1));
	}

	/**
	 * Set next error registry record status and setup error
	 *
	 * @param record Registry record to update
	 * @param error  ImportError
	 * @throws TransitionNotAllowed if error transition is not allowed
	 */
    @Override
	public void setNextErrorStatus(RegistryRecord record, ImportError error) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(record));
		if (allowedCodes.size() < 2) {
			throw new TransitionNotAllowed("No error transition, current is: '" + code(record));
		}

		markRegistryAsHavingError(record);
		setNextStatus(record, allowedCodes.get(1));
		ImportError oldError = record.getImportError();
		if (oldError != null) {
			oldError.copy(error);
		} else {
			record.setImportError(error);
		}
	}

	private void markRegistryAsHavingError(RegistryRecord record) throws TransitionNotAllowed {

		log.debug("Setting record errorous: {}", record);

		registryWorkflowManager.markProcessingHasError(record.getRegistry());
	}

	/**
	 * Set next success registry record status
	 *
	 * @param record Registry record to update
	 * @throws TransitionNotAllowed if success transition is not allowed
	 */
    @Override
	public void setNextSuccessStatus(RegistryRecord record) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(record));
		if (allowedCodes.size() < 1) {
			throw new TransitionNotAllowed("No success transition");
		}

		if (code(record) == PROCESSED_WITH_ERROR) {
			record = removeError(record);
		}

		setNextStatus(record, allowedCodes.get(0));
	}

	/**
	 * Set next success registry records status
	 *
	 * @param records Registry records to update
	 * @throws org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed
	 *          if success transition is not allowed for some of the records
	 */
	@Override
	public void setNextSuccessStatus(Collection<RegistryRecord> records) throws TransitionNotAllowed {

		for (RegistryRecord record : records) {
			setNextSuccessStatus(record);
		}
	}

    @Override
    public void setNextStatusForErrorRecords(@NotNull Collection<RegistryRecord> records) throws TransitionNotAllowed {
        log.info("For this implementation this method not allowed");
    }

    /**
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
    @Override
	public RegistryRecord removeError(RegistryRecord record) {
		if (record.getImportError() == null) {
			return record;
		}

		// disable error
		ImportError error = record.getImportError();
		error.disable();

		return record;
	}

	/**
	 * Set initial registry record status
	 *
	 * @param record Registry record to update
	 * @return SpRegistryRecord back
	 * @throws TransitionNotAllowed if registry already has a status
	 */
    @Override
	public RegistryRecord setInitialStatus(RegistryRecord record) throws TransitionNotAllowed {
		if (record.getRecordStatus() != null) {
			if (code(record) != LOADED) {
				throw new TransitionNotAllowed("Registry was already processed, cannot set initial status");
			}

			return record;
		}

		record.setRecordStatus(statusService.findByCode(LOADED));
		return record;
	}

	/**
	 * Set next registry record status
	 *
	 * @param record Registry record to update
	 * @param code   Next status code to set
	 * @throws TransitionNotAllowed if transition from old to a new status is not allowed
	 */
	private void setNextStatus(RegistryRecord record, Integer code) throws TransitionNotAllowed {
		RegistryRecordStatus status = statusService.findByCode(code);
		setNextStatus(record, status);
	}

	/**
	 * Set next registry record status
	 *
	 * @param record Registry record to update
	 * @param status Next status to set
	 * @throws TransitionNotAllowed if transition from old to a new status is not allowed
	 */
    @Override
	public void setNextStatus(RegistryRecord record, RegistryRecordStatus status) throws TransitionNotAllowed {
		if (!canTransit(record, status)) {
			throw new TransitionNotAllowed("Invalid transition request, was " + record.getRecordStatus() + ", requested " + status);
		}

		record.setRecordStatus(status);
	}

    @Required
	public void setStatusService(RegistryRecordStatusService statusService) {
		this.statusService = statusService;
	}

    @Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}
}
