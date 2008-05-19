package org.flexpay.eirc.persistence.workflow;

import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.SpRegistryStatus;

import static org.flexpay.eirc.persistence.SpRegistryStatus.*;
import org.flexpay.eirc.service.SpRegistryStatusService;
import org.flexpay.eirc.dao.SpRegistryDao;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Helper class for registries workflow
 */
@Transactional (readOnly = true, rollbackFor = Exception.class)
public class RegistryWorkflowManager {

	private Logger log = Logger.getLogger(getClass());

	private SpRegistryStatusService statusService;
	private SpRegistryDao registryDao;

	// allowed transitions from source status code to target codes
	// first status in lists is the successfull one, the second is transition with some processing error
	private Map<Integer, List<Integer>> transitions = new HashMap<Integer, List<Integer>>();

	private Set<Integer> processingStates = new HashSet<Integer>();
	private Set<Integer> transitionsToProcessing = new HashSet<Integer>();

	{
		List<Integer> targets = new ArrayList<Integer>();
		targets.add(LOADED);
		targets.add(LOADED_WITH_ERROR);
		transitions.put(LOADING, targets);
		transitionsToProcessing.add(LOADED);

		transitions.put(LOADED_WITH_ERROR, Collections.<Integer>emptyList());

		targets = new ArrayList<Integer>();
		targets.add(PROCESSING);
		transitions.put(LOADED, targets);

		targets = new ArrayList<Integer>();
		targets.add(PROCESSED);
		targets.add(PROCESSING_WITH_ERROR);
		targets.add(PROCESSING_CANCELED);
		transitions.put(PROCESSING, targets);
		processingStates.add(PROCESSING);

		targets = new ArrayList<Integer>();
		targets.add(ROLLBACKING);
		transitions.put(PROCESSED, targets);

		targets = new ArrayList<Integer>();
		targets.add(PROCESSED_WITH_ERROR);
		targets.add(PROCESSING_CANCELED);
		transitions.put(PROCESSING_WITH_ERROR, targets);
		processingStates.add(PROCESSING_WITH_ERROR);

		targets = new ArrayList<Integer>();
		targets.add(PROCESSING);
		targets.add(ROLLBACKING);
		transitions.put(PROCESSED_WITH_ERROR, targets);
		transitionsToProcessing.add(PROCESSED_WITH_ERROR);

		targets = new ArrayList<Integer>();
		targets.add(PROCESSING);
		targets.add(ROLLBACKING);
		transitions.put(PROCESSING_CANCELED, targets);
		transitionsToProcessing.add(PROCESSING_CANCELED);

		targets = new ArrayList<Integer>();
		targets.add(ROLLBACKED);
		transitions.put(ROLLBACKING, targets);

		targets = new ArrayList<Integer>();
		targets.add(PROCESSING);
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
	public boolean canTransit(SpRegistry registry, SpRegistryStatus nextStatus) {

		return transitions.get(code(registry)).contains(nextStatus.getCode());
	}

	/**
	 * Check if registry can be processed, i.e. has one of the following statuses: {@link SpRegistryStatus#LOADED},
	 * {@link SpRegistryStatus#ROLLBACKED}, {@link SpRegistryStatus#PROCESSING_CANCELED} or {@link SpRegistryStatus#PROCESSED_WITH_ERROR}
	 *
	 * @param registry Registry to check
	 * @return <code>true</code> if registry is allowed to be processed
	 */
	public boolean canProcess(SpRegistry registry) {
		return transitionsToProcessing.contains(code(registry));
	}

	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void startProcessing(SpRegistry registry) throws TransitionNotAllowed {
		if (!canProcess(registry)) {
			throw new TransitionNotAllowed("Cannot start registry processing, invalid status");
		}

		setNextSuccessStatus(registry);
	}

	/**
	 * Helper status code extractor
	 *
	 * @param registry Registry
	 * @return registry status
	 */
	private Integer code(SpRegistry registry) {
		return registry.getRegistryStatus().getCode();
	}

	/**
	 * Set next error registry status
	 *
	 * @param registry Registry to update
	 * @throws TransitionNotAllowed if error transition is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextErrorStatus(SpRegistry registry) throws TransitionNotAllowed {
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
	 * @throws TransitionNotAllowed if success transition is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextSuccessStatus(SpRegistry registry) throws TransitionNotAllowed {
		List<Integer> allowedCodes = transitions.get(code(registry));
		if (allowedCodes.size() < 1) {
			throw new TransitionNotAllowed("No success transition");
		}

		setNextStatus(registry, allowedCodes.get(0));
	}

	/**
	 * Set initial registry status
	 *
	 * @param registry Registry to update
	 * @throws TransitionNotAllowed if registry already has a status
	 * @return SpRegistry back
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public SpRegistry setInitialStatus(SpRegistry registry) throws TransitionNotAllowed {
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
	 * @param code Next status code to set
	 * @throws TransitionNotAllowed if transition from old to a new status is not allowed
	 */
	private void setNextStatus(SpRegistry registry, Integer code) throws TransitionNotAllowed {
		SpRegistryStatus status = statusService.findByCode(code);
		setNextStatus(registry, status);
	}

	/**
	 * Set next registry status
	 *
	 * @param registry Registry to update
	 * @param status Next status to set
	 * @throws TransitionNotAllowed if transition from old to a new status is not allowed
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void setNextStatus(SpRegistry registry, SpRegistryStatus status) throws TransitionNotAllowed {
		if (!canTransit(registry, status)) {
			throw new TransitionNotAllowed("Invalid transition request, was " + registry.getRegistryStatus() + ", requested " + status);
		}

		registry.setRegistryStatus(status);
		registryDao.update(registry);
	}

	public void setStatusService(SpRegistryStatusService statusService) {
		this.statusService = statusService;
	}

	public void setRegistryDao(SpRegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	/**
	 * Set registry processing status to {@link SpRegistryStatus#PROCESSING_WITH_ERROR}
	 * @param spRegistry Registry to update
	 * @throws TransitionNotAllowed if registry status is not {@link SpRegistryStatus#PROCESSING} or {@link SpRegistryStatus#PROCESSING_WITH_ERROR}  
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	void markProcessingHasError(SpRegistry spRegistry) throws TransitionNotAllowed {
		Integer code = code(spRegistry);
		if (!processingStates.contains(code)) {
			throw new TransitionNotAllowed("Cannot mark not processing registry as having errors");
		}

		if (log.isDebugEnabled()) {
			log.debug("Setting registry errorous: " + spRegistry);
		}

		if (code == PROCESSING) {
			log.debug("Updating registry status");
			setNextErrorStatus(spRegistry);
		} else {
			if (log.isDebugEnabled()) {
				log.info("Not updating registry status, current is " + code);
			}
		}
	}
}
