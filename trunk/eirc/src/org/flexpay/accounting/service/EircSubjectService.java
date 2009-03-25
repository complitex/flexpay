package org.flexpay.accounting.service;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Stub;
import org.flexpay.accounting.persistence.EircSubject;

public interface EircSubjectService {

	/**
	 * Read EircSubject object by Stub
	 *
	 * @param subjectStub subject stub
	 * @return EircSubject object
	 */
	public EircSubject read(@NotNull Stub<EircSubject> subjectStub);

	/**
	 * Save subject
	 *
	 * @param eircSubject EircSubject Object
	 */
	public void save(@NotNull EircSubject eircSubject);

	/**
	 * Delete EircSubject object
	 *
	 * @param subjectStub subject stub
	 */
	public void delete(@NotNull Stub<EircSubject> subjectStub);

}
