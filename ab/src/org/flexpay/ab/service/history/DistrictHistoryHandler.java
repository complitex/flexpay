package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class DistrictHistoryHandler extends HistoryHandlerBase<District> {

	private DistrictService districtService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(District.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		District object;

		// find object if it already exists
		Stub<District> stub = correctionsService.findCorrection(
				masterIndex, District.class, masterIndexService.getMasterSourceDescription());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = districtService.readFull(stub);
			} else {
				object = new District();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = districtService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			districtService.disable(CollectionUtils.list(object));
		} else if (object.isNew()) {
			districtService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			districtService.update(object);
		}
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
