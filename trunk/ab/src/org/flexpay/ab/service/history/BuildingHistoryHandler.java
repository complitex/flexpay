package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.list;

public class BuildingHistoryHandler extends HistoryHandlerBase<Building> {

	private ObjectsFactory factory;
	private BuildingService buildingService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	@Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Building.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	@Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		Building object;

		// find object if it already exists
		Stub<Building> stub = correctionsService.findCorrection(
				masterIndex, Building.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = buildingService.readFull(stub);
			} else {
				object = factory.newBuilding();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = buildingService.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			buildingService.disable(collectionIds(list(object)));
		} else if (object.isNew()) {
			buildingService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			buildingService.update(object);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        buildingService.setJpaTemplate(jpaTemplate);
        super.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}
}
