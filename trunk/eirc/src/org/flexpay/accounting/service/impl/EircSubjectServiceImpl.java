package org.flexpay.accounting.service.impl;

import org.flexpay.accounting.dao.EircSubjectDao;
import org.flexpay.accounting.persistence.EircSubject;
import org.flexpay.accounting.service.EircSubjectService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class EircSubjectServiceImpl implements EircSubjectService {

	@NonNls
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
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
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
