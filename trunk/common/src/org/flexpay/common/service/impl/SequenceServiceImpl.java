package org.flexpay.common.service.impl;

import org.flexpay.common.dao.SequenceDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Sequence;
import org.flexpay.common.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class SequenceServiceImpl implements SequenceService {

	private final Logger log = LoggerFactory.getLogger(SequenceServiceImpl.class);

	private Long sequenceCounter = -1L;

	private SequenceDao sequenceDao;

	/**
	 * @param sequenceId id of Sequence
	 * @return next counter value for Sequence
	 */
	synchronized public long next(Long sequenceId) {
		Sequence sequence = sequenceDao.read(sequenceId);
		if (sequence == null) {
			throw new RuntimeException("Can not find sequence with id=" + sequenceId);
		}
		if (sequenceCounter < sequence.getCounter()) {
			sequenceCounter = sequence.getCounter();
		}
		sequence.setCounter(++sequenceCounter);
		updateSequence(sequence);
		log.debug("Updated sequence: {}", sequence);
		return sequenceCounter;
	}

	@Transactional(readOnly = false)
	private void updateSequence(Sequence sequence) {
		sequenceDao.update(sequence);
	}

	@Required
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
}
