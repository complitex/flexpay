package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.list;

public class AddressAttributeTypeHistoryHandler extends HistoryHandlerBase<AddressAttributeType> {

	private AddressAttributeTypeService typeService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	@Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(AddressAttributeType.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	@Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		AddressAttributeType object;

		// find object if it already exists
		Stub<AddressAttributeType> typeStub = correctionsService.findCorrection(
				masterIndex, AddressAttributeType.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (typeStub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = typeService.readFull(typeStub);
			} else {
				object = new AddressAttributeType();
			}
		} else {
			if (typeStub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = typeService.readFull(typeStub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			typeService.disable(collectionIds(list(object)));
		} else if (object.isNew()) {
			typeService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			typeService.update(object);
		}
	}

	@Required
	public void setTypeService(AddressAttributeTypeService typeService) {
		this.typeService = typeService;
	}

}
