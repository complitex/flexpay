package org.flexpay.common.service.imp;

import org.flexpay.common.dao.SequenceDao;
import org.flexpay.common.persistence.Sequence;
import org.flexpay.common.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SequenceServiceImpl implements SequenceService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SequenceDao sequenceDao;

	/**
	 * @param sequenceId id of Sequence
	 * @return next counter value for Sequence
	 */
	@Transactional(readOnly = false)
	public Long next(Long sequenceId) {
		Sequence sequence;
		Long next;
		synchronized (sequenceId) {
			sequence = sequenceDao.read(sequenceId);
			next = sequence.getCounter() + 1;
			sequence.setCounter(next);
			sequenceDao.update(sequence);
		}
		log.debug("Updated sequence: {}", sequence);

		return next;
	}

	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
}
