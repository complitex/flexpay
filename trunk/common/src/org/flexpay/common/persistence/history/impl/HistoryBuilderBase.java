package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.builder.HistoryBuilderHelper;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isEmpty;

public abstract class HistoryBuilderBase<T extends DomainObject> implements HistoryBuilder<T> {

	protected MasterIndexService masterIndexService;
	private ClassToTypeRegistry typeRegistry;
	protected CorrectionsService correctionsService;
	protected HistoryBuilderHelper builderHelper;

	/**
	 * Create diff from t1 to t2
	 *
	 * @param t1 First object
	 * @param t2 Second object
	 * @return Diff object
	 */
	@NotNull
    @Override
	public final Diff diff(@Nullable T t1, @NotNull T t2) {

		Diff diff = new Diff();
		diff.setUserName(SecurityUtil.getUserName());
		diff.setObjectId(t2.getId());
		diff.setObjectType(typeRegistry.getType(t2.getClass()));
		diff.setOperationTime(new Date());
		diff.setInstanceId(ApplicationConfig.getInstanceId());

		if (t1 == null) {
			diff.setOperationType(HistoryOperationType.TYPE_CREATE);
			if (t2.isNotNew()) {
				String masterIndex = SyncContext.isSyncing() ?
									 SyncContext.getProcessingDiff().getMasterIndex() :
									 masterIndexService.getMasterIndex(t2);
				if (masterIndex == null) {
					masterIndex = masterIndexService.getNewMasterIndex(t2);
				}
				diff.setMasterIndex(masterIndex);
				correctionsService.save(new DataCorrection(
						masterIndex, t2.getId(),
						typeRegistry.getType(t2.getClass()),
						new DataSourceDescription(masterIndexService.getMasterSourceDescriptionStub())));
			}
		} else {
			diff.setOperationType(HistoryOperationType.TYPE_UPDATE);
			if (t2.isNotNew()) {
				diff.setMasterIndex(masterIndexService.getMasterIndex(t2));
			}
            if (isEmpty(diff.getMasterIndex())) {
                diff.setMasterIndex(masterIndexService.getNewMasterIndex(t2));
            }
		}

		doDiff(t1, t2, diff);

		return diff;
	}

	/**
	 * Create diff for deleted object
	 *
	 * @param obj object to build diff for
	 * @return Diff object
	 */
	@NotNull
    @Override
	public Diff deleteDiff(@NotNull T obj) {

		Diff diff = new Diff();
		diff.setUserName(SecurityUtil.getUserName());
		diff.setObjectId(obj.getId());
		diff.setObjectType(typeRegistry.getType(obj.getClass()));
		diff.setOperationTime(new Date());
		diff.setInstanceId(ApplicationConfig.getInstanceId());
		diff.setOperationType(HistoryOperationType.TYPE_DELETE);

		String masterIndex = masterIndexService.getMasterIndex(obj);
		if (masterIndex == null) {
			masterIndex = masterIndexService.getNewMasterIndex(obj);
		}
		diff.setMasterIndex(masterIndex);

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

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        masterIndexService.setJpaTemplate(jpaTemplate);
        correctionsService.setJpaTemplate(jpaTemplate);
        builderHelper.setJpaTemplate(jpaTemplate);
    }

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

	@Required
	public void setBuilderHelper(HistoryBuilderHelper builderHelper) {
		this.builderHelper = builderHelper;
	}
}
