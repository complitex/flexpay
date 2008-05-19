package org.flexpay.eirc.persistence.workflow;

import org.flexpay.common.dao.ImportErrorDao;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.eirc.dao.SpRegistryRecordDao;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.persistence.SpRegistryRecordStatus;
import static org.flexpay.eirc.persistence.SpRegistryRecordStatus.*;
import org.flexpay.eirc.service.SpRegistryRecordStatusService;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Helper class for registry records workflow
 */
@Transactional(readOnly = true)
public class RegistryRecordWorkflowManager {

	private Logger log = Logger.getLogger(getClass());
	private RegistryWorkflowManager registryWorkflowManager;

	private SpRegistryRecordStatusService statusService;
	private SpRegistryRecordDao recordDao;
	private ImportErrorDao errorDao;

	private Map<Integer, List<Integer>> transitions = new HashMap<Integer, List<Integer>>();

	{
		List<Integer> targets = new ArrayList<Integer>();
		targets.add(PROCESSED);
		targets.add(PROCESSED_WITH_ERROR);
		transitions.put(LOADED, targets);

		targets = new ArrayList<Integer>();
		targets.add(FIXED);
		transitions.put(PROCESSED_WITH_ERROR, targets);

		targets = new ArrayList<Integer>();
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
	public boolean canTransit(SpRegistryRecord record, SpRegistryRecordStatus nextStatus) {

		return transitions.get(code(record)).contains(nextStatus.getCode());
	}

	public boolean hasSuccessTransition(SpRegistryRecord record) {
		return transitions.get(code(record)).size() > 0;
	}

	/**
	 * Check if registry record is in state that allows processing, or moves it to an allowed one.
	 *
	 * @param record Registry record to start
	 * @throws TransitionNotAllowed if record processing is not possible
	 */
	@Transactional(readOnly = false)
	public void startProcessing(SpRegistryRecord record) throws TransitionNotAllowed {
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
	private Integer code(SpRegistryRecord record) {
		return record.getRecordStatus().getCode();
	}

	/**
	 * Set next error registry record status
	 *
	 * @param record Registry record to update
	 * @throws TransitionNotAllowed if error transition is not allowed
	 */
	@Transactional(readOnly = false)
	public void setNextErrorStatus(SpRegistryRecord record) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(record));
		if (allowedCodes.size() < 2) {
			throw new TransitionNotAllowed("No error transition");
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
	@Transactional(readOnly = false)
	public void setNextErrorStatus(SpRegistryRecord record, ImportError error) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(record));
		if (allowedCodes.size() < 2) {
			throw new TransitionNotAllowed("No error transition");
		}

		markRegistryAsHavingError(record);
		errorDao.create(error);
		record.setImportError(error);
		setNextStatus(record, allowedCodes.get(1));
	}

	private void markRegistryAsHavingError(SpRegistryRecord record) throws TransitionNotAllowed {
		if (log.isDebugEnabled()) {
			log.debug("Setting record errorous: " + record);
		}

		registryWorkflowManager.markProcessingHasError(record.getSpRegistry());
	}

	/**
	 * Set next success registry record status
	 *
	 * @param record Registry record to update
	 * @throws TransitionNotAllowed if success transition is not allowed
	 */
	@Transactional(readOnly = false)
	public void setNextSuccessStatus(SpRegistryRecord record) throws TransitionNotAllowed {
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
	 * Set record status to fixed and invalidate error
	 *
	 * @param record Registry record
	 * @return updated record
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord removeError(SpRegistryRecord record) {
		if (record.getImportError() == null) {
			return record;
		}

		// disable error
		ImportError error = record.getImportError();
		error.setStatus(ObjectWithStatus.STATUS_DISABLED);
		errorDao.update(error);

		// remove error and set status to FIXED
		record.setImportError(null);

		return record;
	}

	/**
	 * Set initial registry record status
	 *
	 * @param record Registry record to update
	 * @return SpRegistryRecord back
	 * @throws TransitionNotAllowed if registry already has a status
	 */
	@Transactional(readOnly = false)
	public SpRegistryRecord setInitialStatus(SpRegistryRecord record) throws TransitionNotAllowed {
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
	private void setNextStatus(SpRegistryRecord record, Integer code) throws TransitionNotAllowed {
		SpRegistryRecordStatus status = statusService.findByCode(code);
		setNextStatus(record, status);
	}

	/**
	 * Set next registry record status
	 *
	 * @param record Registry record to update
	 * @param status Next status to set
	 * @throws TransitionNotAllowed if transition from old to a new status is not allowed
	 */
	@Transactional(readOnly = false)
	public void setNextStatus(SpRegistryRecord record, SpRegistryRecordStatus status) throws TransitionNotAllowed {
		if (!canTransit(record, status)) {
			throw new TransitionNotAllowed("Invalid transition request, was " + record.getRecordStatus() + ", requested " + status);
		}

		record.setRecordStatus(status);
		recordDao.update(record);
	}

	public void setStatusService(SpRegistryRecordStatusService statusService) {
		this.statusService = statusService;
	}

	public void setRecordDao(SpRegistryRecordDao recordDao) {
		this.recordDao = recordDao;
	}

	public void setErrorDao(ImportErrorDao errorDao) {
		this.errorDao = errorDao;
	}

	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}
}
