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
	private boolean processingStarted = false;
	private boolean processingEnded = false;
	private List<RegistryRecord> operationRecords = CollectionUtils.list();
	private List<DelayedUpdate> currentRecordUpdates = CollectionUtils.list();
	private List<DelayedUpdate> operationUpdates = CollectionUtils.list();

	private RegistryRecord currentRecord;

    private String sourceInstanceId;

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

	public boolean isProcessingStarted() {
		return processingStarted;
	}

	public boolean isProcessingEnded() {
		return processingEnded;
	}

	public void startProcessing() {
		processingStarted = true;
	}

	public void endProcessing() {
		processingEnded = true;
	}

	public RegistryRecord getCurrentRecord() {
		return currentRecord;
	}

	public void setCurrentRecord(RegistryRecord currentRecord) throws FlexPayException {
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

	public void failedRecord(RegistryRecord failedRecord) {
		operationRecords.remove(failedRecord);
	}

	public List<RegistryRecord> getOperationRecords() {
		return operationRecords;
	}

    public void setSourceInstanceId(String sourceInstanceId) {
        this.sourceInstanceId = sourceInstanceId;
    }

    public String getSourceInstanceId() {
        return sourceInstanceId;
    }

    public void doUpdate() throws FlexPayExceptionContainer, FlexPayException {
		for (DelayedUpdate update : operationUpdates) {
			update.doUpdate();
		}
		for (DelayedUpdate update : currentRecordUpdates) {
			update.doUpdate();
		}
	}

	public void visitCurrentRecordUpdates(DelayedUpdateVisitor visitor) throws FlexPayException {

		for (DelayedUpdate update : currentRecordUpdates) {
			visitor.apply(update);
		}
	}

	public void visitOperationUpdates(DelayedUpdateVisitor visitor) throws FlexPayException {

		for (DelayedUpdate update : operationUpdates) {
			visitor.apply(update);
		}
	}

	public void addUpdate(DelayedUpdate update) {

		if (update == DelayedUpdateNope.INSTANCE) {
			return;
		}
		if (update instanceof DelayedUpdatesContainer) {
			for (DelayedUpdate childUpdate : ((DelayedUpdatesContainer) update).getUpdates()) {
				addUpdate(childUpdate);
			}
			return;
		}
		currentRecordUpdates.add(update);
	}

	public void beforeUpdate() throws FlexPayException {

		visitOperationUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) throws FlexPayException {
				if (update instanceof UpdatesListener) {
					((UpdatesListener) update).beforeUpdate(ProcessingContext.this);
				}
			}
		});
	}
}
