package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.list;

public class TownTypeHistoryHandler extends HistoryHandlerBase<TownType> {

	private TownTypeService townTypeService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	@Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(TownType.class) == diff.getObjectType();
	}

	/**
	 * Apply diff to the system object
	 *
	 * @param diff Diff
	 */
	@Override
	public void process(@NotNull Diff diff) throws Exception {
		String masterIndex = diff.getMasterIndex();
		TownType object;

		// find object if it already exists
		Stub<TownType> typeStub = correctionsService.findCorrection(
				masterIndex, TownType.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (typeStub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = townTypeService.readFull(typeStub);
			} else {
				object = new TownType();
			}
		} else {
			if (typeStub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = townTypeService.readFull(typeStub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			townTypeService.disable(collectionIds(list(object)));
		} else if (object.isNew()) {
			townTypeService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			townTypeService.update(object);
		}
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        townTypeService.setJpaTemplate(jpaTemplate);
        super.setJpaTemplate(jpaTemplate);
    }


	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}
