package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import java.util.Date;

public abstract class HistoryBuilderBase<T extends DomainObject> implements HistoryBuilder<T> {

	protected MasterIndexService masterIndexService;
	private ClassToTypeRegistry typeRegistry;
	protected CorrectionsService correctionsService;

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
	public final Diff diff(@Nullable T t1, @NotNull T t2) {

		Diff diff = new Diff();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			diff.setUserName(authentication.getName());
		}
		diff.setObjectId(t2.getId());
		diff.setObjectType(typeRegistry.getType(t2.getClass()));
		diff.setOperationTime(new Date());

		if (t1 == null) {
			diff.setOperationType(HistoryOperationType.TYPE_CREATE);
			if (t2.isNotNew()) {
				diff.setMasterIndex(masterIndexService.getNewMasterIndex(t2));
				correctionsService.save(new DataCorrection(
						diff.getMasterIndex(), t2.getId(),
						typeRegistry.getType(t2.getClass()),
						masterIndexService.getMasterSourceDescription()));
			}
		} else {
			diff.setOperationType(HistoryOperationType.TYPE_UPDATE);
		}

		doDiff(t1, t2, diff);

		return diff;
	}

	/**
	 * Build necessary diff records
	 *
	 * @param t1   First object
	 * @param t2   Second object
	 * @param diff Diff object
	 */
	protected abstract void doDiff(@Nullable T t1, @NotNull T t2, @NotNull Diff diff);

	@Required
	public void setTypeRegistry(ClassToTypeRegistry typeRegistry) {
		this.typeRegistry = typeRegistry;
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}
}
