package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.service.SubdivisionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class SubdivisionHistoryHandler extends HistoryHandlerBase<Subdivision> {

	private SubdivisionService subdivisionService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Subdivision.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 * @throws Exception if failure occurs
	 */
    @Override
	public void process(@NotNull Diff diff) throws Exception {

		String masterIndex = diff.getMasterIndex();
		Subdivision object;

		// find object if it already exists
		Stub<Subdivision> stub = correctionsService.findCorrection(
				masterIndex, Subdivision.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = subdivisionService.read(stub);
			} else {
				object = new Subdivision();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = subdivisionService.read(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			subdivisionService.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			subdivisionService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			subdivisionService.update(object);
		}
	}

	@Required
	public void setSubdivisionService(SubdivisionService subdivisionService) {
		this.subdivisionService = subdivisionService;
	}
}
