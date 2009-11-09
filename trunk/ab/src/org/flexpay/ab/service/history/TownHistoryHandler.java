package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.service.TownService;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TownHistoryHandler extends HistoryHandlerBase<Town> {

	private TownService townService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	@Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Town.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	@Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		Town object;

		// find object if it already exists
		Stub<Town> stub = correctionsService.findCorrection(
				masterIndex, Town.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = townService.readFull(stub);
			} else {
				object = new Town();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = townService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			townService.disable(collectionIds(list(object)));
		} else if (object.isNew()) {
			townService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			townService.update(object);
		}
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
