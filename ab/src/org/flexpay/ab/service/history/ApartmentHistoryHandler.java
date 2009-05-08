package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ApartmentHistoryHandler extends HistoryHandlerBase<Apartment> {

	private ApartmentService apartmentService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Apartment.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		Apartment object;

		// find object if it already exists
		Stub<Apartment> stub = correctionsService.findCorrection(
				masterIndex, Apartment.class, masterIndexService.getMasterSourceDescription());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = apartmentService.readFull(stub);
			} else {
				object = new Apartment();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = apartmentService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			stub = new Stub<Apartment>(object);
			apartmentService.disable(CollectionUtils.list(stub.getId()));
		} else if (object.isNew()) {
			apartmentService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			apartmentService.update(object);
		}
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}
}
