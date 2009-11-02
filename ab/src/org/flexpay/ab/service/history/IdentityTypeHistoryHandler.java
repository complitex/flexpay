package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.service.IdentityTypeService;
import static org.flexpay.common.persistence.DomainObject.collectionIds;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class IdentityTypeHistoryHandler extends HistoryHandlerBase<IdentityType> {

	private IdentityTypeService identityTypeService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(IdentityType.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		IdentityType object;

		// find object if it already exists
		Stub<IdentityType> typeStub = correctionsService.findCorrection(
				masterIndex, IdentityType.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (typeStub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = identityTypeService.read(typeStub);
			} else {
				object = new IdentityType();
			}
		} else {
			if (typeStub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = identityTypeService.read(typeStub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			identityTypeService.disable(collectionIds(list(object)));
		} else if (object.isNew()) {
			identityTypeService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			identityTypeService.update(object);
		}
	}

	@Required
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}
}
