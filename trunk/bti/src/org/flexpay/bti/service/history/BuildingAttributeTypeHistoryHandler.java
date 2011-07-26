package org.flexpay.bti.service.history;

import org.flexpay.bti.persistence.building.BuildingAttributeGroup;
import org.flexpay.bti.persistence.building.BuildingAttributeType;
import org.flexpay.bti.persistence.building.BuildingAttributeTypeSimple;
import org.flexpay.bti.service.BuildingAttributeTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingAttributeTypeHistoryHandler extends HistoryHandlerBase<BuildingAttributeType> {

	private BuildingAttributeTypeService attributeTypeService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(BuildingAttributeGroup.class) == diff.getObjectType();
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
		BuildingAttributeType object;

		// find object if it already exists
		Stub<BuildingAttributeType> stub = correctionsService.findCorrection(
				masterIndex, BuildingAttributeType.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = attributeTypeService.readFull(stub);
			} else {
				String className = diff.getObjectTypeName();
				try {
					object = (BuildingAttributeType) Class.forName(className).newInstance();
				} catch (Exception ex) {
					log.error("failed creating new type " + className + ", continue with simple type ", ex);
					object = new BuildingAttributeTypeSimple();
				}
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = attributeTypeService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			attributeTypeService.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			attributeTypeService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			attributeTypeService.update(object);
		}
	}

	@Required
	public void setAttributeTypeService(BuildingAttributeTypeService attributeTypeService) {
		this.attributeTypeService = attributeTypeService;
	}
}
