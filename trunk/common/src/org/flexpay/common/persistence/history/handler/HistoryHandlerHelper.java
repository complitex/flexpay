package org.flexpay.common.persistence.history.handler;

import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.service.DomainObjectService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.MasterIndexService;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class HistoryHandlerHelper {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private MasterIndexService masterIndexService;

	public <T extends DomainObject> void process(Diff diff, HistoryBuilder<T> historyBuilder,
												 DomainObjectService<T> service, CorrectionsService correctionsService)
			throws Exception {

		String masterIndex = diff.getMasterIndex();
		T object;

		// find object if it already exists
		Stub<? extends T> stub = correctionsService.findCorrection(
				masterIndex, service.getType(), masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = service.readFull(stub);
			} else {
				object = service.newInstance();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = service.readFull(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			service.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			service.create(object);
			saveMasterCorrection(object, diff, correctionsService);
		} else {
			service.update(object);
		}
	}

	private void saveMasterCorrection(DomainObject obj, Diff diff, CorrectionsService correctionsService) {
		DataCorrection correction = correctionsService.getStub(
				diff.getMasterIndex(), obj, masterIndexService.getMasterSourceDescriptionStub());
		log.debug("Saving master correction: {}", correction);
		correctionsService.save(correction);
	}

	@Required
	public void setMasterIndexService(MasterIndexService masterIndexService) {
		this.masterIndexService = masterIndexService;
	}
}
