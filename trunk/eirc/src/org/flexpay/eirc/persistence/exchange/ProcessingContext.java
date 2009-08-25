package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdatesContainer;

import java.util.List;

public class ProcessingContext {

	private Registry registry;
	private List<RegistryRecord> operationRecords = CollectionUtils.list();
	private List<DelayedUpdate> currentRecordUpdates = CollectionUtils.list();
	private List<DelayedUpdate> operationUpdates = CollectionUtils.list();

	private RegistryRecord currentRecord;

	/**
	 * Do cleanup of all delayed updates
	 */
	public void nextOperation() {
		operationRecords.clear();
		currentRecordUpdates.clear();
		operationUpdates.clear();
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public RegistryRecord getCurrentRecord() {
		return currentRecord;
	}

	public void setCurrentRecord(RegistryRecord currentRecord) {
		this.currentRecord = currentRecord;
		operationRecords.add(currentRecord);
		operationUpdates.addAll(currentRecordUpdates);

		visitCurrentRecordUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
				if (update instanceof UpdatesListener) {
					((UpdatesListener) update).nextRecord(ProcessingContext.this);
				}
			}
		});

		currentRecordUpdates.clear();

	}

	public List<RegistryRecord> getOperationRecords() {
		return operationRecords;
	}

	public void doUpdate() throws FlexPayExceptionContainer, FlexPayException {
		for (DelayedUpdate update : operationUpdates) {
			update.doUpdate();
		}
	}

	public void visitCurrentRecordUpdates(DelayedUpdateVisitor visitor) {

		for (DelayedUpdate update : currentRecordUpdates) {
			visitor.apply(update);
		}
	}

	public void visitOperationUpdates(DelayedUpdateVisitor visitor) {

		for (DelayedUpdate update : operationUpdates) {
			visitor.apply(update);
		}
	}

	public void addUpdate(DelayedUpdate update) {

		if (update == DelayedUpdateNope.INSTANCE) {
			return;
		}
		if (update instanceof DelayedUpdatesContainer) {
			List<DelayedUpdate> updates = ((DelayedUpdatesContainer) update).getUpdates();
			for (DelayedUpdate childUpdate : updates) {
				addUpdate(childUpdate);
			}
			return;
		}
		currentRecordUpdates.add(update);
	}

	public void beforeUpdate() {

		visitOperationUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
				if (update instanceof UpdatesListener) {
					((UpdatesListener) update).beforeUpdate(ProcessingContext.this);
				}
			}
		});
	}
}
