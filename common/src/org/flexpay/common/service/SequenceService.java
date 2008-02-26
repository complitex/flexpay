package org.flexpay.common.service;


public interface SequenceService {
	/**
	 * @param sequenceId id of Sequence
	 * @return next counter value for Sequence
	 */
	Long next(Long sequenceId);

}
