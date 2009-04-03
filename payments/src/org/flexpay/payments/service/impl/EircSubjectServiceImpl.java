package org.flexpay.payments.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.EircSubjectDao;
import org.flexpay.payments.persistence.EircSubject;
import org.flexpay.payments.service.EircSubjectService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class EircSubjectServiceImpl implements EircSubjectService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private EircSubjectDao eircSubjectDao;

	/**
	 * Read EircSubject object by Stub
	 *
	 * @param subjectStub subject stub
	 * @return EircSubject object
	 */
	@Nullable
	public EircSubject read(@NotNull Stub<EircSubject> subjectStub) {
		return eircSubjectDao.readFull(subjectStub.getId());
	}

	/**
	 * Save subject
	 *
	 * @param eircSubject EircSubject Object
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull EircSubject eircSubject) {
		if (eircSubject.isNew()) {
			eircSubject.setId(null);
			eircSubjectDao.create(eircSubject);
		} else {
			eircSubjectDao.update(eircSubject);
		}
	}

	/**
	 * Delete EircSubject object
	 *
	 * @param subjectStub subject stub
	 */
	@Transactional (readOnly = false)
	public void delete(@NotNull Stub<EircSubject> subjectStub) {
		EircSubject eircSubject = eircSubjectDao.read(subjectStub.getId());

		if (eircSubject == null) {
			log.debug("Can't find eirc subject with id {}", subjectStub.getId());
			return;
		}

		eircSubjectDao.delete(eircSubject);
	}

	@Required
	public void setEircSubjectDao(EircSubjectDao eircSubjectDao) {
		this.eircSubjectDao = eircSubjectDao;
	}

}
