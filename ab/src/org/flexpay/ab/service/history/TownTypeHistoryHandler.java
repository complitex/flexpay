package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class TownTypeHistoryHandler extends HistoryHandlerBase<TownType> {

	private TownTypeService townTypeService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(TownType.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		TownType object;

		// find object if it already exists
		Stub<TownType> typeStub = correctionsService.findCorrection(
				masterIndex, TownType.class, masterIndexService.getMasterSourceDescription());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (typeStub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = townTypeService.read(typeStub.getId());
			} else {
				object = new TownType();
			}
		} else {
			if (typeStub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = townTypeService.read(typeStub.getId());
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			townTypeService.disable(CollectionUtils.list(object));
		} else if (object.isNew()) {
			townTypeService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			townTypeService.update(object);
		}
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}
