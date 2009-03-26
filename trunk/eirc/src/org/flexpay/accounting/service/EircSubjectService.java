package org.flexpay.accounting.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.flexpay.common.persistence.Stub;
import org.flexpay.accounting.persistence.EircSubject;
import org.springframework.security.annotation.Secured;

public interface EircSubjectService {

	/**
	 * Read EircSubject object by Stub
	 *
	 * @param subjectStub subject stub
	 * @return EircSubject object
	 */
	@Secured (Roles.EIRC_SUBJECT_READ)
	@Nullable
	public EircSubject read(@NotNull Stub<EircSubject> subjectStub);

	/**
	 * Save subject
	 *
	 * @param eircSubject EircSubject Object
	 */
	@Secured ({Roles.EIRC_SUBJECT_ADD, Roles.EIRC_SUBJECT_CHANGE})
	public void save(@NotNull EircSubject eircSubject);

	/**
	 * Delete EircSubject object
	 *
	 * @param subjectStub subject stub
	 */
	@Secured (Roles.EIRC_SUBJECT_DELETE)
	public void delete(@NotNull Stub<EircSubject> subjectStub);

}
