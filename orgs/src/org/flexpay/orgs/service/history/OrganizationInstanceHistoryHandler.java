package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.OrganizationInstance;
import org.flexpay.orgs.persistence.OrganizationInstanceDescription;
import org.flexpay.orgs.service.OrganizationInstanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public abstract class OrganizationInstanceHistoryHandler<
		D extends OrganizationInstanceDescription,
		T extends OrganizationInstance<D, T>> extends HistoryHandlerBase<T> {

	private OrganizationInstanceService<D, T> instanceService;

	protected abstract Class<T> getType();

	protected abstract T newInstance();

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 * @throws Exception if failure occurs
	 */
    @Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		T object;

		// find object if it already exists
		Stub<T> stub = correctionsService.findCorrection(
				masterIndex, getType(), masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = instanceService.read(stub);
			} else {
				object = newInstance();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = instanceService.read(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			instanceService.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			instanceService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			instanceService.update(object);
		}
	}

	@Required
	public void setInstanceService(OrganizationInstanceService<D, T> instanceService) {
		this.instanceService = instanceService;
	}
}
