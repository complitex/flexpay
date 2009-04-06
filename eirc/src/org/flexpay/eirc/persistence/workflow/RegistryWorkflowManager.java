package org.flexpay.eirc.persistence.workflow;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.eirc.dao.RegistryDaoExt;
import static org.flexpay.common.persistence.registry.RegistryStatus.*;
import org.flexpay.eirc.service.SpRegistryStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for registries workflow
 */
@Transactional (readOnly = true)
public class RegistryWorkflowManager {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SpRegistryStatusService statusService;
	private RegistryDao registryDao;
	private RegistryDaoExt registryDaoExt;

	// allowed transitions from source status code to target codes
	// first status in lists is the successfull one, the second is transition with some processing error
	private Map<Integer, List<Integer>> transitions = CollectionUtils.map();

	private Set<Integer> processingStates = CollectionUtils.set();
	private Set<Integer> transitionsToProcessing = CollectionUtils.set();

	{
		List<Integer> targets = CollectionUtils.list(LOADED, LOADED_WITH_ERROR);
		transitions.put(LOADING, targets);
		transitionsToProcessing.add(LOADED);

		transitions.put(LOADED_WITH_ERROR, Collections.<Integer>emptyList());

		targets = CollectionUtils.list(PROCESSING);
		transitions.put(LOADED, targets);

		targets = CollectionUtils.list(PROCESSED, PROCESSING_WITH_ERROR, PROCESSING_CANCELED);
		transitions.put(PROCESSING, targets);
		processingStates.add(PROCESSING);

		// allow set processed with errors if there are any not processed records
		targets = CollectionUtils.list(ROLLBACKING, PROCESSED_WITH_ERROR);
		transitions.put(PROCESSED, targets);

		targets = CollectionUtils.list(PROCESSED_WITH_ERROR, PROCESSING_CANCELED);
		transitions.put(PROCESSING_WITH_ERROR, targets);
		processingStates.add(PROCESSING_WITH_ERROR);

		targets = CollectionUtils.list(PROCESSING, ROLLBACKING);
		transitions.put(PROCESSED_WITH_ERROR, targets);
		transitionsToProcessing.add(PROCESSED_WITH_ERROR);

		targets = CollectionUtils.list(PROCESSING, ROLLBACKING);
		transitions.put(PROCESSING_CANCELED, targets);
		transitionsToProcessing.add(PROCESSING_CANCELED);

		targets = CollectionUtils.list(ROLLBACKED);
		transitions.put(ROLLBACKING, targets);

		targets = CollectionUtils.list(PROCESSING);
		transitions.put(ROLLBACKED, targets);
		transitionsToProcessing.add(ROLLBACKED);
	}

	/**
	 * Check if registry status transition allowed
	 *
	 * @param registry   Registry to check
	 * @param nextStatus Registry status to set up
	 * @return <code>true</code> if registry processing allowed, or <code>false</code> otherwise
	 */
	public boolean canTransit(Registry registry, RegistryStatus nextStatus) {

		return transitions.get(code(registry)).contains(nextStatus.getCode());
	}

	/**
	 * Check if registry can be processed, i.e. has one of the following statuses: {@link
	 * org.flexpay.common.persistence.registry.RegistryStatus#LOADED}, {@link org.flexpay.common.persistence.registry.RegistryStatus#ROLLBACKED},
	 * {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_CANCELED} or {@link
	 * org.flexpay.common.persistence.registry.RegistryStatus#PROCESSED_WITH_ERROR}
	 *
	 * @param registry Registry to check
	 * @return <code>true</code> if registry is allowed to be processed
	 */
	public boolean canProcess(Registry registry) {
		return transitionsToProcessing.contains(code(registry));
	}

	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void startProcessing(Registry registry) throws TransitionNotAllowed {
		if (!canProcess(registry)) {
			throw new TransitionNotAllowed("Cannot start registry processing, invalid status: " +
										   registry.getRegistryStatus().getI18nName());
		}

		setNextSuccessStatus(registry);
	}

	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void endProcessing(Registry registry) throws TransitionNotAllowed {
		// all records processed
		if (code(registry) == PROCESSED) {
			try {
				if (registryDaoExt.hasMoreRecordsToProcess(registry.getId())) {
					setNextStatus(registry, PROCESSED_WITH_ERROR);
				}
			} catch (Throwable t) {
				setNextStatus(registry, PROCESSED_WITH_ERROR);
				log.error("Unexpected error", t);
				throw new RuntimeException("Unexpected error when ending processing", t);
			}
		}
	}

	/**
	 * Helper status code extractor
	 *
	 * @param registry Registry
	 * @return registry status
	 */
	private Integer code(Registry registry) {
		return registry.getRegistryStatus().getCode();
	}

	/**
	 * Set next error registry status
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if error transition is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextErrorStatus(Registry registry) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(registry));
		if (allowedCodes.size() < 2) {
			throw new TransitionNotAllowed("No error transition");
		}

		setNextStatus(registry, allowedCodes.get(1));
	}

	/**
	 * Set next success registry status
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if success transition is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextSuccessStatus(Registry registry) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(registry));
		if (allowedCodes.size() < 1) {
			throw new TransitionNotAllowed("No success transition, current is: " +
										   registry.getRegistryStatus().getI18nName());
		}

		setNextStatus(registry, allowedCodes.get(0));
	}

	/**
	 * Set initial registry status
	 *
	 * @param registry Registry to update
	 * @return SpRegistry back
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if registry already has a status
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public Registry setInitialStatus(Registry registry) throws TransitionNotAllowed {
		if (registry.getRegistryStatus() != null) {
			if (code(registry) != LOADING) {
				throw new TransitionNotAllowed("Registry was already processed, cannot set initial status");
			}

			return registry;
		}

		registry.setRegistryStatus(statusService.findByCode(LOADING));
		return registry;
	}

	/**
	 * Set next registry status
	 *
	 * @param registry Registry to update
	 * @param code	 Next status code to set
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if transition from old to a new status is not allowed
	 */
	private void setNextStatus(Registry registry, Integer code) throws TransitionNotAllowed {
		RegistryStatus status = statusService.findByCode(code);
		setNextStatus(registry, status);
	}

	/**
	 * Set next registry status
	 *
	 * @param registry Registry to update
	 * @param status   Next status to set
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if transition from old to a new status is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextStatus(Registry registry, RegistryStatus status) throws TransitionNotAllowed {
		if (!canTransit(registry, status)) {
			throw new TransitionNotAllowed("Invalid transition request, was " + registry.getRegistryStatus() + ", requested " + status);
		}

		registry.setRegistryStatus(status);
		registryDao.update(registry);
	}

	public void setStatusService(SpRegistryStatusService statusService) {
		this.statusService = statusService;
	}

	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	public void setRegistryDaoExt(RegistryDaoExt registryDaoExt) {
		this.registryDaoExt = registryDaoExt;
	}

	/**
	 * Set registry processing status to {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_WITH_ERROR}
	 *
	 * @param registry Registry to update
	 * @throws org.flexpay.eirc.persistence.workflow.TransitionNotAllowed
	 *          if registry status is not {@link org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING} or {@link
	 *          org.flexpay.common.persistence.registry.RegistryStatus#PROCESSING_WITH_ERROR}
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	void markProcessingHasError(Registry registry) throws TransitionNotAllowed {
		Integer code = code(registry);
		if (!processingStates.contains(code)) {
			throw new TransitionNotAllowed("Cannot mark not processing registry as having errors");
		}

		log.debug("Setting registry errorous: {}", registry);

		if (code == PROCESSING) {
			log.debug("Updating registry status");
			setNextErrorStatus(registry);
		} else {
			log.debug("Not updating registry status, current is {}", code);
		}
	}
}
