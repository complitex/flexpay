package org.flexpay.common.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.dao.SequenceDao;
import org.flexpay.common.persistence.Sequence;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SequenceServiceImpl {
	private static Logger log = Logger.getLogger(SequenceServiceImpl.class);

	private SequenceDao sequenceDao;

	/**
	 * @param sequenceId
	 *            id of Sequence
	 * @return next counter value for Sequence
	 */
	@Transactional(readOnly = false)
	public Long next(Long sequenceId) {
		Sequence sequence = sequenceDao.read(sequenceId);
		Long next = sequence.getCounter() + 1;
		sequence.setCounter(next);
		sequenceDao.update(sequence);
		if (log.isDebugEnabled()) {
			log.debug("Updated sequence: " + sequence);
		}

		return next;
	}

	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

}
