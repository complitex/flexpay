package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayException;


public interface SequenceService {
	
	/**
	 * id of sequence for personal account
	 */
	Long PERSONAL_ACCOUNT_SEQUENCE_ID = 1L;
	
	/**
	 * @param sequenceId id of Sequence
	 * @return next counter value for Sequence
	 */
	long next(Long sequenceId);

}
