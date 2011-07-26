package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class OrganizationHistoryHandler extends HistoryHandlerBase<Organization> {

	private OrganizationService organizationService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Organization.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
    @Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		Organization object;

		// find object if it already exists
		Stub<Organization> stub = correctionsService.findCorrection(
				masterIndex, Organization.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = organizationService.readFull(stub);
			} else {
				object = new Organization();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = organizationService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			organizationService.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			organizationService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			organizationService.update(object);
		}
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
