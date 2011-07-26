package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryOperationType;
import org.flexpay.common.persistence.history.impl.HistoryHandlerBase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.service.CashboxService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CashboxHistoryHandler extends HistoryHandlerBase<Cashbox> {

	private CashboxService cashboxService;

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(Cashbox.class) == diff.getObjectType();
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
		Cashbox object;

		// find object if it already exists
		Stub<Cashbox> stub = correctionsService.findCorrection(
				masterIndex, Cashbox.class, masterIndexService.getMasterSourceDescriptionStub());

		if (diff.getOperationType() == HistoryOperationType.TYPE_CREATE) {
			if (stub != null) {
				log.info("Request for object creation, but it already exists {}", diff);
				object = cashboxService.read(stub);
			} else {
				object = new Cashbox();
			}
		} else {
			if (stub == null) {
				log.warn("Requested for object update/delete, but not found {}", diff);
				throw new IllegalStateException("Requested for object update/delete, but not found " + masterIndex);
			}
			object = cashboxService.read(stub);
		}

		if (object == null) {
			throw new IllegalStateException("Existing correction is invalid, object not found: " + masterIndex);
		}

		historyBuilder.patch(object, diff);

		if (diff.getOperationType() == HistoryOperationType.TYPE_DELETE) {
			cashboxService.disable(CollectionUtils.set(object.getId()));
		} else if (object.isNew()) {
			cashboxService.create(object);
			saveMasterCorrection(object, diff);
		} else {
			cashboxService.update(object);
		}
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

}
