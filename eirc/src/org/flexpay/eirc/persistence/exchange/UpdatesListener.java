package org.flexpay.eirc.persistence.exchange;

/**
 * Marker interface for delayed updates that need to perform additional actions before update is done
 */
public interface UpdatesListener {

	void nextRecord(ProcessingContext context);

	void beforeUpdate(ProcessingContext context);
}
