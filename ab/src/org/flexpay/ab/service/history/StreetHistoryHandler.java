package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class StreetHistoryHandler extends HistoryHandlerBase<Street> {

	private StreetService streetService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Street.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		Street object;

		// find object if it already exists
		Stub<Street> stub = correctionsService.findCorrection(
				masterIndex, Street.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = streetService.readFull(stub);
			} else {
				object = new Street();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = streetService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			streetService.disable(CollectionUtils.list(object));
		} else if (object.isNew()) {
			streetService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			streetService.update(object);
		}
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
